登录失败：
* 用户名/密码错误
* 密码错误

前台/后台标识：

**请求头：**
前台：'X-Client-Type': 'Frontend',
后台：'X-Client-Type': 'Backend'

第三方gitee登录
1.用户授权，拿到access_token
2.重定向到首页并且携带access_token
3.根据access_token获取用户信息

