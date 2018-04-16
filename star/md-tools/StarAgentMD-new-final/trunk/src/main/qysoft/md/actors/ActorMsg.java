package main.qysoft.md.actors;

/**
 * Created by kevin on 2017/10/22.
 */
public enum ActorMsg {
    CLOSE,                              //关闭Actor
    TRANS_RESULT_SUCCESS,               //交易结果--成功
    TRANS_START,                        //开始交易
    TRANS_RESULT_FAIL,                  //交易结果--失败
    TRANS_RESULT_EMPTY_QUEUE,           //交易结果--交易队列为空
    TRANS_RESULT_PRICE_NOT_MATCH        //交易结果--价格不匹配
}
