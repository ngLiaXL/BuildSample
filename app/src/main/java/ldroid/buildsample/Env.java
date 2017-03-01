package ldroid.buildsample;

public enum Env {
    /**
     * 开发环境
     */
    debug("debug",
          "debug",
          "debug"),

    /**
     * 测试环境
     */
    release("release",
            "release",
            "release"),

    /**
     * 生产环境
     */
    product("product",
            "product",
            "product");


    private static final Env env;

    public final String sdUrl;
    public final String oaUrl;
    public final String gdUrl;


    static {
        env = Env.valueOf(BuildConfig.BUILD_TYPE);
    }


    Env(String sdUrl, String oaUrl, String gdUrl) {
        this.sdUrl = sdUrl;
        this.oaUrl = oaUrl;
        this.gdUrl = gdUrl;
    }

    public static Env get() {
        return env;
    }

}