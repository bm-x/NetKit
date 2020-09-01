#Netkit2.0

##致力于提供一种更加简洁、高效、直观、明朗的网络请求库

```kotlin
    request {
        url = "http://www.baidu.com"
        query["name"] = "123"
        query["age"] = 18
        method = "GET"

        asynchronized = false

        header = {
            Accept = "xml"
            Accept_Encoding = "utf8"
            Content_Length = 128
        }

        // 通过简单的加号链接，随意制定方法回调的运行线程 Default：默认线程   UI：ui线程  executors：指定某个线程池  Thread：另起线程
        finish = executors + {

        }

        before_success = Default + {

        }

        // 通过装饰器将数据转换为自己想的数据类型，如json转对象
        success = UI + objectDecoration<NkIgnore> {

        }

        error = Thread + {

        }
    }
```
兼容的1.0方式
```kotlin
  // get https://www.api.com/example/get?key1=value1&key2=value2
  get("https://www.api.com/example/get").objectConvert<TargetObject>()
          .loadingDialogStyle1(this).errorHandler()
          .pageArrayHanler(listview) // 对分页状态，上下拉刷新的处理
          .querys("key1","value1")
          .querys("key2","value2")
          .params("key1","value1") // post
          .headers("key","value)
          .onStart { req, ignore -> }
          .onSuccess { target, bundle, req, res, ignore -> }
          .onError { error, ignore -> }
          .onFinish { req, ignore -> }
          .end()
 ```
