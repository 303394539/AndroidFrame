package com.baic.view;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.JavascriptInterface;
import android.webkit.JsPromptResult;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.baic.utils.JsonUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * Created by baic on 16/5/20.
 */
public class AlphaSafeWebView extends WebView {
    private static final String TAG = "AlphaSelfWebView";
    private Map<String, JsCallJava> mJsCallJavas;
    private Map<Integer, String> mInjectJavaScripts;
    private WebChromeClient alphaWebChromeClient;
    private WebViewClient alphaWebViewClient;

    private Context mContext;

    private String JS_OBJECT = "__AHA_JSSDK";
    private boolean openBrowser = false;
    private boolean verticalScrollBar = false;
    private boolean horizontalScrollBar = false;
    private String userAgentString;
    private JsHelper jsHelper;
    private String mUrl;

    private List<String> safeDomainList = new ArrayList<>();

    public void addSafeDomain(String domain){
        this.safeDomainList.add(domain);
    }

    @Override
    public void setWebViewClient(WebViewClient client) {
        this.alphaWebViewClient = client;
        super.setWebViewClient(client);
    }

    @Override
    public void setWebChromeClient(WebChromeClient client) {
        this.alphaWebChromeClient = client;
        super.setWebChromeClient(client);
    }

    public void setting() {
        WebSettings settings = this.getSettings();
        if (userAgentString != null && !"".equals(userAgentString)) {
            settings.setUserAgentString(userAgentString);
        }

        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            settings.setLoadsImagesAutomatically(true);
        } else {
            settings.setLoadsImagesAutomatically(false);
        }

        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        settings.setDatabaseEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setAllowFileAccess(false);
        settings.setSupportZoom(false);
        settings.setNeedInitialFocus(true);
//        settings.setBuiltInZoomControls(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        settings.setAllowFileAccessFromFileURLs(false);
        settings.setJavaScriptCanOpenWindowsAutomatically(false);

        this.setHorizontalScrollBarEnabled(horizontalScrollBar);
        this.setHorizontalFadingEdgeEnabled(false);
        this.setVerticalScrollBarEnabled(verticalScrollBar);
        this.setVerticalFadingEdgeEnabled(false);

        if(alphaWebViewClient == null) {
            this.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    view.loadUrl(url);
                    return !openBrowser;
                }
            });
        }
        if(alphaWebChromeClient == null){
            this.setWebChromeClient(new WebChromeClient() {
            });
        }

        this.addJavascriptInterface(jsHelper, JS_OBJECT);
        this.removeJavascriptInterface("searchBoxJavaBridge_");
        this.removeJavascriptInterface("accessibility");
        this.removeJavascriptInterface("accessibilityTraversal");
    }

    public void setUserAgentString(String userAgentString) {
        this.userAgentString = userAgentString;
    }

    public abstract static class JsHelper {

        private Context mContext;
        private AlphaSafeWebView alphaSafeWebView;

        public void setAlphaSafeWebView(AlphaSafeWebView alphaSafeWebView) {
            this.alphaSafeWebView = alphaSafeWebView;
        }

        public JsHelper(Context context) {
            this.mContext = context;
        }

        protected void execJsCallBack(String function, Object param, boolean success) {
            if (alphaSafeWebView != null && function != null && !"".equals(function.trim())) {
                function = function.replaceAll(".*([';]+|(--)+).*", "");
                Map<String, Object> map = new HashMap<>();
                map.put("msg", param);
                map.put("success", success);
                String js = String.format("javascript:%s(%s)", function, JsonUtil.objectToString(map));
                Log.e("execJsCallBack:", js);
                alphaSafeWebView.loadUrl(js);
            }
        }

        protected void execJsCallBackSuccess(String function, Object param){
            execJsCallBack(function, param, true);
        }

        protected void execJsCallBackFail(String function, Object param){
            execJsCallBack(function, param, false);
        }

        protected void execJsCallBackComplete(String function, Object param, boolean success){
            execJsCallBack(function, param, success);
        }

        protected void execJsCallBackCancel(String function) {
            execJsCallBack(function, "", false);
        }

        @JavascriptInterface
        public void log4Client(String msg) {
            Log.e("JsHelper log", msg);
        }
    }

    public void setJsHelper(JsHelper helper) {
        this.jsHelper = helper;
        this.jsHelper.setAlphaSafeWebView(this);
    }

    public boolean verifySafeDomain(String url){
        for (String domain : safeDomainList) {
            if (url.indexOf(domain) >= 0) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void loadUrl(String url) {
        if (jsHelper == null) {
            throw new IllegalArgumentException("jsHelper is null");
        }
        if(verifySafeDomain(url) || url.startsWith("javascript:")){
            setting();
            super.loadUrl(url);
        }
    }

    @Override
    public void loadUrl(String url, Map<String, String> additionalHttpHeaders) {
        if (jsHelper == null) {
            throw new IllegalArgumentException("jsHelper is null");
        }
        if(verifySafeDomain(url)){
            setting();
            super.loadUrl(url, additionalHttpHeaders);
        }
    }

    public void loadUrl() {
        if (mUrl == null || "".equals(mUrl)) {
            throw new IllegalArgumentException("url is null");
        }
        loadUrl(mUrl);
    }

    public AlphaSafeWebView(Context context) {
        this(context, null);
        mContext = context;
        readAttrs(null);
    }

    public AlphaSafeWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        readAttrs(attrs);
    }

    public AlphaSafeWebView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        readAttrs(attrs);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public AlphaSafeWebView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        readAttrs(attrs);
    }

    private void readAttrs(AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = getContext().obtainStyledAttributes(
                    attrs,
                    R.styleable.AlphaSafeWebView,
                    0, 0);
            if (typedArray != null) {
                if (typedArray.hasValue(R.styleable.AlphaSafeWebView_jsObject)) {
                    JS_OBJECT = typedArray.getString(R.styleable.AlphaSafeWebView_jsObject);
                }
                mUrl = typedArray.getString(R.styleable.AlphaSafeWebView_url);
                openBrowser = typedArray.getBoolean(R.styleable.AlphaSafeWebView_openBrowser, false);
                verticalScrollBar = typedArray.getBoolean(R.styleable.AlphaSafeWebView_verticalScrollBar, false);
                horizontalScrollBar = typedArray.getBoolean(R.styleable.AlphaSafeWebView_horizontalScrollBar, false);
                typedArray.recycle();
            }
        }
    }

    @Override
    public void destroy() {
        if (mJsCallJavas != null) {
            mJsCallJavas.clear();
        }
        if (mInjectJavaScripts != null) {
            mInjectJavaScripts.clear();
        }
        removeAllViews();
        //WebView中包含一个ZoomButtonsController，当使用web.getSettings().setBuiltInZoomControls(true);启用该设置后，用户一旦触摸屏幕，就会出现缩放控制图标。这个图标过上几秒会自动消失，但在3.0系统以上上，如果图标自动消失前退出当前Activity的话，就会发生ZoomButton找不到依附的Window而造成程序崩溃，解决办法很简单就是在Activity的ondestory方法中调用web.setVisibility(View.GONE);方法，手动将其隐藏，就不会崩溃了。在3.0一下系统上不会出现该崩溃问题，真是各种崩溃，防不胜防啊！
        setVisibility(GONE);
        ViewParent parent = getParent();
        if (parent instanceof ViewGroup) {
            ViewGroup mWebViewContainer = (ViewGroup) getParent();
            mWebViewContainer.removeAllViews();
        }
        releaseConfigCallback();
        super.destroy();
    }

    /**
     * 添加并注入JavaScript脚本（和“addJavascriptInterface”注入对象的注入时机一致，100%能注入成功）；
     * 注意：为了做到能100%注入，需要在注入的js中自行判断对象是否已经存在（如：if (typeof(window.Android) = 'undefined')）；
     *
     * @param javaScript
     */
    public void addInjectJavaScript(String javaScript) {
        if (mInjectJavaScripts == null) {
            mInjectJavaScripts = new HashMap<Integer, String>();
        }
        mInjectJavaScripts.put(javaScript.hashCode(), javaScript);
        injectExtraJavaScript();
    }

    private void injectJavaScript() {
        for (Map.Entry<String, JsCallJava> entry : mJsCallJavas.entrySet()) {
            this.loadUrl(buildNotRepeatInjectJS(entry.getKey(), entry.getValue().getPreloadInterfaceJS()));
        }
    }

    private void injectExtraJavaScript() {
        for (Map.Entry<Integer, String> entry : mInjectJavaScripts.entrySet()) {
            this.loadUrl(buildTryCatchInjectJS(entry.getValue()));
        }
    }

    /**
     * 构建一个“不会重复注入”的js脚本；
     *
     * @param key
     * @param js
     * @return
     */
    public String buildNotRepeatInjectJS(String key, String js) {
        String obj = String.format("__injectFlag_%1$s__", key);
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{(function(){if(window.");
        sb.append(obj);
        sb.append("){console.log('");
        sb.append(obj);
        sb.append(" has been injected');return;}window.");
        sb.append(obj);
        sb.append("=true;");
        sb.append(js);
        sb.append("}())}catch(e){console.warn(e)}");
        return sb.toString();
    }

    /**
     * 构建一个“带try catch”的js脚本；
     *
     * @param js
     * @return
     */
    public String buildTryCatchInjectJS(String js) {
        StringBuilder sb = new StringBuilder();
        sb.append("javascript:try{");
        sb.append(js);
        sb.append("}catch(e){console.warn(e)}");
        return sb.toString();
    }

    // 解决WebView内存泄漏问题；
    private void releaseConfigCallback() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) { // JELLY_BEAN
            try {
                Field field = WebView.class.getDeclaredField("mWebViewCore");
                field = field.getType().getDeclaredField("mBrowserFrame");
                field = field.getType().getDeclaredField("sConfigCallback");
                field.setAccessible(true);
                field.set(null, null);
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        } else {
            try {
                Field sConfigCallback = Class.forName("android.webkit.BrowserFrame").getDeclaredField("sConfigCallback");
                if (sConfigCallback != null) {
                    sConfigCallback.setAccessible(true);
                    sConfigCallback.set(null, null);
                }
            } catch (NoSuchFieldException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (ClassNotFoundException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            } catch (IllegalAccessException e) {
                if (BuildConfig.DEBUG) {
                    e.printStackTrace();
                }
            }
        }
    }

    static class JsCallJava {
        private final static String TAG = "JsCallJava";
        private final static String RETURN_RESULT_FORMAT = "{\"code\": %d, \"result\": %s}";
        private static final String MSG_PROMPT_HEADER = "SafeWebView:";
        private static final String KEY_OBJ = "obj";
        private static final String KEY_METHOD = "method";
        private static final String KEY_TYPES = "types";
        private static final String KEY_ARGS = "args";
        private static final String[] IGNORE_UNSAFE_METHODS = {"getClass", "hashCode", "notify", "notifyAll", "equals", "toString", "wait"};
        private HashMap<String, Method> mMethodsMap;
        private Object mInterfaceObj;
        private String mInterfacedName;
        private String mPreloadInterfaceJS;

        public JsCallJava(Object interfaceObj, String interfaceName) {
            try {
                if (TextUtils.isEmpty(interfaceName)) {
                    throw new Exception("injected name can not be null");
                }
                mInterfaceObj = interfaceObj;
                mInterfacedName = interfaceName;
                mMethodsMap = new HashMap<String, Method>();
                // getMethods会获得所有继承与非继承的方法
                Method[] methods = mInterfaceObj.getClass().getMethods();
                // 拼接的js脚本可参照备份文件：./library/doc/injected.js
                StringBuilder sb = new StringBuilder("javascript:(function(b){console.log(\"");
                sb.append(mInterfacedName);
                sb.append(" init begin\");var a={queue:[],callback:function(){var d=Array.prototype.slice.call(arguments,0);var c=d.shift();var e=d.shift();this.queue[c].apply(this,d);if(!e){delete this.queue[c]}}};");
                for (Method method : methods) {
                    String sign;
                    if ((sign = genJavaMethodSign(method)) == null) {
                        continue;
                    }
                    mMethodsMap.put(sign, method);
                    sb.append(String.format("a.%s=", method.getName()));
                }
                sb.append("function(){var f=Array.prototype.slice.call(arguments,0);if(f.length<1){throw\"");
                sb.append(mInterfacedName);
                sb.append(" call error, message:miss method name\"}var e=[];for(var h=1;h<f.length;h++){var c=f[h];var j=typeof c;e[e.length]=j;if(j==\"function\"){var d=a.queue.length;a.queue[d]=c;f[h]=d}}var k = new Date().getTime();var l = f.shift();var m=prompt('");
                sb.append(MSG_PROMPT_HEADER);
                sb.append("'+JSON.stringify(");
                sb.append(promptMsgFormat("'" + mInterfacedName + "'", "l", "e", "f"));
                sb.append("));console.log(\"invoke \"+l+\", time: \"+(new Date().getTime()-k));var g=JSON.parse(m);if(g.code!=200){throw\"");
                sb.append(mInterfacedName);
                sb.append(" call error, code:\"+g.code+\", message:\"+g.result}return g.result};Object.getOwnPropertyNames(a).forEach(function(d){var c=a[d];if(typeof c===\"function\"&&d!==\"callback\"){a[d]=function(){return c.apply(a,[d].concat(Array.prototype.slice.call(arguments,0)))}}});b.");
                sb.append(mInterfacedName);
                sb.append("=a;console.log(\"");
                sb.append(mInterfacedName);
                sb.append(" init end\")})(window)");
                mPreloadInterfaceJS = sb.toString();
                sb.setLength(0);
            } catch (Exception e) {
                if (BuildConfig.DEBUG) {
                    Log.e(TAG, "init js error:" + e.getMessage());
                }
            }
        }

        private String genJavaMethodSign(Method method) {
            String sign = method.getName();
            Class[] argsTypes = method.getParameterTypes();
            for (String ignoreMethod : IGNORE_UNSAFE_METHODS) {
                if (ignoreMethod.equals(sign)) {
                    if (BuildConfig.DEBUG) {
                        Log.w(TAG, "method(" + sign + ") is unsafe, will be pass");
                    }
                    return null;
                }
            }
            int len = argsTypes.length;
            for (int k = 0; k < len; k++) {
                Class cls = argsTypes[k];
                if (cls == String.class) {
                    sign += "_S";
                } else if (cls == int.class ||
                        cls == long.class ||
                        cls == float.class ||
                        cls == double.class) {
                    sign += "_N";
                } else if (cls == boolean.class) {
                    sign += "_B";
                } else if (cls == JSONObject.class) {
                    sign += "_O";
                } else if (cls == JsCallback.class) {
                    sign += "_F";
                } else {
                    sign += "_P";
                }
            }
            return sign;
        }

        public String getPreloadInterfaceJS() {
            return mPreloadInterfaceJS;
        }

        public String call(WebView webView, JSONObject jsonObject) {
            long time = 0;
            if (BuildConfig.DEBUG) {
                time = android.os.SystemClock.uptimeMillis();
            }
            if (jsonObject != null) {
                try {
                    String methodName = jsonObject.getString(KEY_METHOD);
                    JSONArray argsTypes = jsonObject.getJSONArray(KEY_TYPES);
                    JSONArray argsVals = jsonObject.getJSONArray(KEY_ARGS);
                    String sign = methodName;
                    int len = argsTypes.length();
                    Object[] values = new Object[len];
                    int numIndex = 0;
                    String currType;

                    for (int k = 0; k < len; k++) {
                        currType = argsTypes.optString(k);
                        if ("string".equals(currType)) {
                            sign += "_S";
                            values[k] = argsVals.isNull(k) ? null : argsVals.getString(k);
                        } else if ("number".equals(currType)) {
                            sign += "_N";
                            numIndex = numIndex * 10 + k + 1;
                        } else if ("boolean".equals(currType)) {
                            sign += "_B";
                            values[k] = argsVals.getBoolean(k);
                        } else if ("object".equals(currType)) {
                            sign += "_O";
                            values[k] = argsVals.isNull(k) ? null : argsVals.getJSONObject(k);
                        } else if ("function".equals(currType)) {
                            sign += "_F";
                            values[k] = new JsCallback(webView, mInterfacedName, argsVals.getInt(k));
                        } else {
                            sign += "_P";
                        }
                    }

                    Method currMethod = mMethodsMap.get(sign);

                    // 方法匹配失败
                    if (currMethod == null) {
                        return getReturn(jsonObject, 500, "not found method(" + sign + ") with valid parameters", time);
                    }
                    // 数字类型细分匹配
                    if (numIndex > 0) {
                        Class[] methodTypes = currMethod.getParameterTypes();
                        int currIndex;
                        Class currCls;
                        while (numIndex > 0) {
                            currIndex = numIndex - numIndex / 10 * 10 - 1;
                            currCls = methodTypes[currIndex];
                            if (currCls == int.class) {
                                values[currIndex] = argsVals.getInt(currIndex);
                            } else if (currCls == long.class) {
                                //WARN: argsJson.getLong(k + defValue) will return a bigger incorrect number
                                values[currIndex] = Long.parseLong(argsVals.getString(currIndex));
                            } else {
                                values[currIndex] = argsVals.getDouble(currIndex);
                            }
                            numIndex /= 10;
                        }
                    }

                    return getReturn(jsonObject, 200, currMethod.invoke(mInterfaceObj, values), time);
                } catch (Exception e) {
                    //优先返回详细的错误信息
                    if (e.getCause() != null) {
                        return getReturn(jsonObject, 500, "method execute error:" + e.getCause().getMessage(), time);
                    }
                    return getReturn(jsonObject, 500, "method execute error:" + e.getMessage(), time);
                }
            } else {
                return getReturn(jsonObject, 500, "call data empty", time);
            }
        }

        private String getReturn(JSONObject reqJson, int stateCode, Object result, long time) {
            String insertRes;
            if (result == null) {
                insertRes = "null";
            } else if (result instanceof String) {
                result = ((String) result).replace("\"", "\\\"");
                insertRes = "\"".concat(String.valueOf(result)).concat("\"");
            } else { // 其他类型直接转换
                insertRes = String.valueOf(result);

                // 兼容：如果在解决WebView注入安全漏洞时，js注入采用的是XXX:function(){return prompt(...)}的形式，函数返回类型包括：void、int、boolean、String；
                // 在返回给网页（onJsPrompt方法中jsPromptResult.confirm）的时候强制返回的是String类型，所以在此将result的值加双引号兼容一下；
                // insertRes = "\"".concat(String.valueOf(result)).concat("\"");
            }
            String resStr = String.format(RETURN_RESULT_FORMAT, stateCode, insertRes);
            if (BuildConfig.DEBUG) {
                Log.d(TAG, "call time: " + (android.os.SystemClock.uptimeMillis() - time) + ", request: " + reqJson + ", result:" + resStr);
            }
            return resStr;
        }

        private static String promptMsgFormat(String object, String method, String types, String args) {
            StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(KEY_OBJ).append(":").append(object).append(",");
            sb.append(KEY_METHOD).append(":").append(method).append(",");
            sb.append(KEY_TYPES).append(":").append(types).append(",");
            sb.append(KEY_ARGS).append(":").append(args);
            sb.append("}");
            return sb.toString();
        }

        /**
         * 是否是“Java接口类中方法调用”的内部消息；
         *
         * @param message
         * @return
         */
        static boolean isSafeWebViewCallMsg(String message) {
            return message.startsWith(MSG_PROMPT_HEADER);
        }

        static JSONObject getMsgJSONObject(String message) {
            message = message.substring(MSG_PROMPT_HEADER.length());
            JSONObject jsonObject;
            try {
                jsonObject = new JSONObject(message);
            } catch (JSONException e) {
                e.printStackTrace();
                jsonObject = new JSONObject();
            }
            return jsonObject;
        }

        static String getInterfacedName(JSONObject jsonObject) {
            return jsonObject.optString(KEY_OBJ);
        }
    }

    static class JsCallback {
        private static final String CALLBACK_JS_FORMAT = "javascript:%s.callback(%d, %d %s);";
        private int mIndex;
        private boolean mCouldGoOn;
        private WeakReference<WebView> mWebViewRef;
        private int mIsPermanent;
        private String mInjectedName;

        public JsCallback(WebView view, String injectedName, int index) {
            mCouldGoOn = true;
            mWebViewRef = new WeakReference<WebView>(view);
            mInjectedName = injectedName;
            mIndex = index;
        }

        /**
         * 向网页执行js回调；
         *
         * @param args
         * @throws JsCallbackException
         */
        public void apply(Object... args) throws JsCallbackException {
            if (mWebViewRef.get() == null) {
                throw new JsCallbackException("the WebView related to the JsCallback has been recycled");
            }
            if (!mCouldGoOn) {
                throw new JsCallbackException("the JsCallback isn't permanent,cannot be called more than once");
            }
            StringBuilder sb = new StringBuilder();
            for (Object arg : args) {
                sb.append(",");
                boolean isStrArg = arg instanceof String;
                // 有的接口将Json对象转换成了String返回，这里不能加双引号，否则网页会认为是String而不是JavaScript对象；
                boolean isObjArg = isJavaScriptObject(arg);
                if (isStrArg && !isObjArg) {
                    sb.append("\"");
                }
                sb.append(String.valueOf(arg));
                if (isStrArg && !isObjArg) {
                    sb.append("\"");
                }
            }
            String execJs = String.format(CALLBACK_JS_FORMAT, mInjectedName, mIndex, mIsPermanent, sb.toString());
            if (BuildConfig.DEBUG) {
                Log.d("JsCallBack", execJs);
            }
            mWebViewRef.get().loadUrl(execJs);
            mCouldGoOn = mIsPermanent > 0;
        }

        /**
         * 是否是JSON(JavaScript Object Notation)对象；
         *
         * @param obj
         * @return
         */
        private boolean isJavaScriptObject(Object obj) {
            if (obj instanceof JSONObject || obj instanceof JSONArray) {
                return true;
            } else {
                String json = obj.toString();
                try {
                    new JSONObject(json);
                } catch (JSONException e) {
                    try {
                        new JSONArray(json);
                    } catch (JSONException e1) {
                        return false;
                    }
                }
                return true;
            }
        }

        /**
         * 一般传入到Java方法的js function是一次性使用的，即在Java层jsCallback.apply(...)之后不能再发起回调了；
         * 如果需要传入的function能够在当前页面生命周期内多次使用，请在第一次apply前setPermanent(true)；
         *
         * @param value
         */
        public void setPermanent(boolean value) {
            mIsPermanent = value ? 1 : 0;
        }

        public static class JsCallbackException extends Exception {
            public JsCallbackException(String msg) {
                super(msg);
            }
        }
    }
}
