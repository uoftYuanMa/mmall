package com.mmall.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.Serializable;

/**
 * 通用的服务端响应对象
 * @param <T>
 */

//Json序列化的时候，会调用getStatus()，getData()和getMsg()方法
//由于我们的构造函数导致可能没有msg或者没有data的情况
//这个时候前端的json里就会出现 类似 msg="" 的情况
//用下面的这个注解，可以在序列化的时候，不序列null的情况
//也就是json里没有 msg="" 这一项
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class ServerResponse<T> implements Serializable {
    private int status;
    private String msg;
    private T data;

    private ServerResponse(int status){
        this.status = status;
    }
    //当T是String，会和第四个构造方法混，解决办法在public的实现当中
    private ServerResponse(int status, T data){
        this.status =status;
        this.data =data;
    }
    private ServerResponse(int status,String msg,T data){
        this.status =status;
        this.msg = msg;
        this.data =data;
    }
    private ServerResponse(int status,String msg){
        this.status =status;
        this.msg = msg;
    }
    //Jackson序列化的时候，会调用public方法，用这个注解可以防止此方法被序列化
    @JsonIgnore
    public boolean isSuccess(){
        return this.status == ResponseCode.SUCCESS.getCode();
    }
    public int getStatus(){
        return status;
    }
    public T getData(){
        return data;
    }
    public String getMsg(){
        return msg;
    }
    //静态方法调用非静态私有构造器从而创建对象
    public static <T> ServerResponse<T> createBySuccess(){
        return new ServerResponse<T>(
                ResponseCode.SUCCESS.getCode());
    }
    //2 仔细看2，3调用的构造器，并理解该设计思路
    public static <T> ServerResponse<T> createBySuccessMessage(String msg){
        return new ServerResponse<T>(
                ResponseCode.SUCCESS.getCode(),msg
        );
    }
    //3
    public static <T> ServerResponse<T> createBySuccess(T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode()
        ,data);
    }
    public static <T> ServerResponse<T> createBySuccess(String msg,T data){
        return new ServerResponse<T>(ResponseCode.SUCCESS.getCode(),msg,data);
    }
    //失败
    public static <T>ServerResponse<T> createByError(){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),
                ResponseCode.ERROR.getDesc());
    }
    public static <T> ServerResponse<T> createByErrorMessage(String errorMessage){
        return new ServerResponse<T>(ResponseCode.ERROR.getCode(),
                errorMessage);
    }

    public static <T> ServerResponse<T> createByErrorCodeMessage(int errorCode,
                                                                 String errorMessage){
        return new ServerResponse<T>(errorCode,errorMessage);
    }
}
