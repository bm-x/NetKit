#Netkit

I Hope like this:

```kotlin
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