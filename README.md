#Netkit

##致力于提供一种更加简洁、高效、明朗的网络请求库

```kotlin
    request {
        url = "http://www.baidu.com"
        query["name"] = "123"
        query["age"] = 18
        method = "GET"

        asynchronized = false

        header = {
            Accept = "xml"
            Accept = "html"
        }

        finish = executors + {

        }

        before_success = Default + {

        }

        success = UI + objectDecoration<NkIgnore> {

        }

        error = Thread + {

        }
    }
```
