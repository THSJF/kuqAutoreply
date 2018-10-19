# kuqAutoreply
com/meng/Autoreply.java为主类
功能基本是在其它的类里写好然后在Autoreply里调用
Banner.java禁言指令
BiliVideoInfo.java BilibiliVideoInfoJavaBean.java 哔哩哔哩视频详情
LiveManager.java LivePerson.java 哔哩哔哩直播检测
DicReplyGroup.java DicReplyManager.java是群内按词库内容回复
fanpohai.java反迫害
FileTipManager.java FileTipUploader.java群文件上传提醒
LivingManager.java LivingPerson.java哔哩哔哩直播检测
MengAutoReplyConfig.java 用于读取一些设置
Methods.java 一些没必要开新类的功能
Nai.java 哔哩哔哩直播奶人
RecoderManager.java RecordBanner.java复读和复读小游戏
RollPlane.java 随机选择游戏,机体,难度
ZuiSuJinTianGengLeMa.java 最速今天更了吗
词库是json文件，包含多个json数组。示例：

{
"这里是":["打牌催更群"],
"搬砖":["imageFolder:pic/搬砖/"],
"^((https|http|ftp|rtsp|mms)?:\\/\\/)[^\\s]": ["这网址还行"], 
"去.*打.*[红妖永花风殿船庙城绀璋]": ["image:pic/sjf.jpg"]
}

dic.json为公用的词库，必须内容完全匹配才会触发，dic********.json 为各群独立专用词库，使用正则表达式匹配结果。"搬砖"对应的回答中的"imageFolder"是对非纯文本回答做出的标记，可在Autoreply.java中找到相关代码

config.json也是多个json数组组成，示例：

{
"mapGroupNotReply":["826536230"],
"mapQQNotReply":["2756253478"],
"mapWordNotReply":[],
"mapGroupRecorder":["807242547","855927922","439664871","424838564","857548607","348595763","859561731","312342896"],
"mapGroupDicReply":["807242547","859561731","348595763","857548607","855927922","439664871","424838564"],
"mapLiveTip":["记者","523030","沙苗","5136443"]
}

key说明：
mapGroupNotReply----指定的群不回复
mapQQNotReply----指定的账号不回复
mapWordNotReply----包含指定的字符串不回复
mapGroupRecorder----开启复读的群
mapGroupDicReply----开启词库回答的群
mapLiveTip----哔哩哔哩直播提示，顺序为 称呼，直播间号，称呼，直播间号，称呼，直播间号……
勿修改key的值，否则会导致功能不正常
如果不需要某个功能，像mapWordNotReply一项那样即可