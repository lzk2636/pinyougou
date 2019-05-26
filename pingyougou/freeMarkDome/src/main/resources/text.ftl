<html>
<head>
    <title>freeMarkDome</title>
    <meta charset="utf-8">
</head>
<body>
名字：${name},消息:${message}<br>
<#assign pname="苹果">
水果：${pname}<br>
<#if suceess=true>
    中国人
    <#else>
    日本人
</#if><br>
<#--日本-->
<#list goodsList as list>
    ${list.name}------${list.price}---------${list_index}---
</#list><br>
<#assign text="{'list':[{'bank':'工商银行','account':'10101920201920212'},{'bank':'工商银行1','account':'110101920201920212'}]}">

<#assign data=text?eval />
<#list data.list as it>
    ${it.bank}<br>
</#list>
今天:${date?date}<br>
时间:${date?time}
${date?string("yyyy年MM月")}<br>
${point?c}
</body>
</html>