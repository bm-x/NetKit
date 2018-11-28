#Netkit

##New

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
