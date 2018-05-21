#Netkit

不喜欢什么retrofit，rxjava等等等等的网络请求方式了

我希望的请求方式是这样的~

```kotlin
  
  // get https://www.api.com/example/get?key1=value1&key2=value2
  get("https://www.api.com/example/get").stringConvert()
          .params("key1","value1")
          .params("key2","value2")
          .onSuccess { target, bundle, req, res, ignore -> }
          .end()
  
  // 表单方式 post
  post("https://www.api.com/example/login").objectConvert<UserBean>()
                .errorHandler()
                .loadingDialog()
                .multipart("userName", "zhangsan")
                .multipart("pwd", "123456")
                .onSuccess { userInstance, bundle, req, res, ignore ->
                    toast("登入成功  user is : ${userInstance}")
                }
                .onError { error, bundle, req, ignore ->
                    // handle error
                }
                .end()
```

or 

```kotlin
    post("https://www.api.com/example/login").objectConvert<UserBean>()
               ........
               .success(::afterLoginSuccess)
               .error(::handleError)
               .end()
                
    fun handleError(error: Throwable) {
         toast("error -> ${error}")
    }
    
    
    // 对于方法引用的回调，不必做参数的完全匹配~想要什么参数就写啥参数即可~
    fun afterLoginSuccess(user: UserBean) {
        toast("登入成功  user is : ${user}")
    }
    fun afterLoginSuccess(user: UserBean,req: NkRequest<UserBean>) {
            toast("登入成功  user is : ${user}")
    }
    // 参数顺序也可随意~
    fun afterLoginSuccess(bundle:NkBundle,req: NkRequest<UserBean>,user: UserBean) {
            toast("登入成功  user is : ${user}")
    }
```

or 

```kotlin
    var user: UserBean? = null 
        set(value) {
            // do something after login
        }
    var bundle: NkBundle? = null
        set(value) {
            // set bundle
        }
            
    post("https://www.api.com/example/login").objectConvert<UserBean>()
            .....
            .success(::user::set) 
            .success(::bundle::set) 
            .end()
```

还有好多地方没弄好，慎用，仅供参考
