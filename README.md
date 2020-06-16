# alipay
支付宝支付
1.先打开alipay.properties，修改里面的参数
2.配置redis和mq的地址
3.启动项目
4.讲本地地址通过nginx映射外网，以支付宝回调地址通顺。
5.访问网页支付接口：http://127.0.0.1:9442/alipay/ali_pay.tml?flow=wap_pay