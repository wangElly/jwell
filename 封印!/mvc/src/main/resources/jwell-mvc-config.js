/**
 * bug 和问题太多了。。 
 * 
 * 都是最初设计的问题，要不要重构呢？
 * 
 * 该提交一下代码了么？
 */

var loader = {
    "com.fenwell.jwell.mvc.api.Loader" : "com.fenwell.jwell.mvc.spi.loader.DefaultClassLoader",
    "com.fenwell.jwell.mvc.api.ActionDriver" : "com.fenwell.jwell.mvc.spi.action.MapperActionDriver",
    "com.fenwell.jwell.mvc.api.ViewHandler" : "com.fenwell.jwell.mvc.spi.view.HttlViewHandler",
    "com.fenwell.jwell.mvc.api.ParamHandler" : "com.fenwell.jwell.mvc.spi.param.DefaultParamHandler",
    "com.fenwell.jwell.mvc.api.ScanHandler" : "com.fenwell.jwell.mvc.spi.scan.ClassScanHandler"
};

var mvc = {
    suffix : ".action",
    split : "/",
    encode : "UTF-8"
};