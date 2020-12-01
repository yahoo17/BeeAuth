package com.Beeyan;


/*
调用方进行接口请求的时候，将 URL、AppID、密码、时间戳拼接在一起，
通过加密算法生成 token，并且将 token、AppID、时间戳拼接在 URL 中，
一并发送到微服务端。微服务端在接收到调用方的接口请求之后，
从请求中拆解出 token、AppID、时间戳。
微服务端首先检查传递过来的时间戳跟当前时间，是否在 token 失效时间窗口内。
如果已经超过失效时间，那就算接口调用鉴权失败，拒绝接口调用请求。
如果 token 验证没有过期失效，微服务端再从自己的存储中，
取出 AppID 对应的密码，通过同样的 token 生成算法，生成另外一个 token，
与调用方传递过来的 token 进行匹配；如果一致，则鉴权成功，允许接口调用，否则就拒绝接口调用。
 */
public class DefaultAuth implements AuthInterface {

    private StorageInstance storageInstance;

    DefaultAuth(){
        this.storageInstance = new MySQLStorageInstance();
    }

    DefaultAuth(StorageInstance storageInstance){
        this.storageInstance = storageInstance;
    }



    @Override
    public boolean Auth(String fullUrl) {
        ApiRequest apiRequest = ApiRequest.BuildFromFullUrl(fullUrl);
        return Auth(apiRequest);
    }


    @Override
    public boolean Auth(ApiRequest request) {
        //token、AppID、时间戳
        String token =  request.getToken();
        String password = request.getPassword();
        String baseUrl = request.getBaseUrl();
        String appId = request.getAppId();
        Long timestamp = request.getTimestamp();
        AuthToken clientAuthToken = new AuthToken(token,timestamp);

        if(clientAuthToken.IsExpire())
            return false;
        else{

            String passwordFromDB =  storageInstance.getPasswordFromAppId(appId);
            if(passwordFromDB == request.getPassword()){
                return true;
            }
            return false;
        }
    }
}
