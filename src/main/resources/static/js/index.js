//一般直接写在一个js文件中,layui的版本是
layui.use(['jquery','tree','element','layer','util','code'], function() {
    var layer = layui.layer,
        util = layui.util,
        $ = layui.jquery,
        element = layui.element();
    //加载代码渲染(高亮代码)
    hljs.initHighlightingOnLoad();
    //获取目录树
    getCategory();
    //监听tab
    element.on('tab(tabfilter)', function(data){
        layer.closeAll();
    });

    $('.layui-tab-content').css('height',$(window).height());

    //固定块(树形菜单弹出)
    var contentTree;
    util.fixbar({
        bar1: '<i class="layui-icon" style="font-size: 30px;">&#xe60f;</i>',
        bar2: '<i class="layui-icon" style="font-size: 30px;">&#xe604;</i>'
        , css: {right: 60, bottom: 10}
        , bgcolor: '#393D49'
        , click: function (type) {
            if (type === 'bar1') {
                var toolWidth = (document.body.clientWidth * 0.03) < 45 ? 45 : (document.body.clientWidth * 0.03);
                var offsetLeft = document.body.clientWidth - toolWidth - 300;
                layer.close(contentTree);
                contentTree = layer.open({
                    area: ['300px', '500px'],
                    offset: ['0px', offsetLeft + 'px'],
                    shade: false,
                    type: 1,
                    content: $('.layui-tab-content .layui-show #content-tree').html()
                });
            }else {
                $('.layui-tab-content .layui-show').animate({scrollTop:0},1000);
            }
        }
    });
});
//获取目录树
function getCategory() {
    var $ = layui.jquery;
    //关闭所有弹窗
    var layer = layui.layer;
    layer.closeAll();
    var $tree = $('#tree-content');
    $tree.html('');
    $.getJSON("/categorys", function (data) {
        //构造节点树
        layui.tree({
            elem: '#tree-content' //指定元素
            , target: '_blank' //是否新选项卡打开（比如节点返回href才有效）
            , click: function (item) { //点击节点回调
                //处理非文件夹
                if (!item.isDir){
                    var $alreadShow = $('#'+item.id);
                    if ($alreadShow.length > 0){
                        $alreadShow.click();
                    }else {
                        getArticle(item.path,item.id,item.name);
                    }
                }
            }
            , nodes: data
        });
    });
}
//文件显示
function getArticle(path,id,name) {
    var $ = layui.jquery;
    var layer = layui.layer;
    var s = "preview-panel"+id;
    var element = layui.element();
    $.ajax({
        type: "POST",
        url: "/article",
        data: {path: path},
        beforeSend:function () {
            index = layer.load(1); //换了种风格
            $('#'+s).empty(1000);
        },
        success: function (result) {

            element.tabAdd('tabfilter', {
                title: name
                ,content: ' <div class="preview-panel" id='+s+'> </div> ' +
                '   <div style="display: none;" id="content-tree"> ' +
                ' <div class="content-tree" > ' +
                '</div></div> '
            });

            //创建tab标签
            $('.layui-this').removeClass('layui-this');
            $('.layui-tab-title li:last').prop({
                id:id,
                title:path
            });


            var $preview = $("#"+s);
            markdownToHtml(result.content, $preview, function(html) {
                //toc标题
                var toc = $('#'+s+'+#content-tree .content-tree');
                toc.html($('#'+s+' .toc').html());

                $("#"+s+' pre').addClass("layui-code");
                $("#"+s+ ' pre code').each(function(i, block) {
                    hljs.highlightBlock(block);
                });
            });
            //切换top
            element.tabChange('tabfilter', $('.layui-tab-title li').length-1);
            //生成文档回到顶部
            $('body,html').animate({scrollTop:0},1000);
        },
        complete:function (XMLHttpRequest, textStatus) {
            layer.close(index);
            //引用code方法
            layui.code({
                elem:'#'+s+' .layui-code',
                skin:'notepad'});
        }
    })
}
//刷新指定文章
function freshArticle() {}
//文件上传弹出层
var upload;
function uploadMd() {}
//文件下载
function downloadMd() {}
//删除文章
function deleteMd() {
    var $ = layui.jquery;
    var layer = layui.layer;
    var confirm;
    //弹层组件-询问框
    layer.confirm('确定要删除？',function (confirmTitle) {
        var $curTab = $('.layui-tab-title .layui-this');
        var path = $curTab.prop('title');
        var index = $curTab.index();
        if(path.length >0){
            $.ajax({
                type: "POST",
                url: "delete",
                dataType:'json',
                data:{path:path},
                beforeSend:function () {
                    confirm = layer.load(1); //换了种风格
                },
                success:function (data) {
                    if (data.status == 0){
                        layui.element().tabDelete('tabfilter', index);
                        $('.fresh-tree button').click();
                        layer.msg(data.msg);
                    }else {
                        layer.msg(data.msg);
                    }
                },
                complete:function (XMLHttpRequest, textStatus) {
                    layer.close(confirm);
                    layer.close(confirmTitle);
                }
            });
        }
    })
}
//文件比较弹出层
var compare;
function compareFile() {
    var $ = layui.jquery;
    var layer = layui.layer;
    layer.close(compare); //关闭
    $.ajax({
        type: "GET",
        url: "/fragment/compare",
        dataType:'html',
        success:function (data) {
            compare = layer.open({
                area: ['850px', '580px'],
                type: 1,
                content: data //这里content是一个普通的String
            });
        }
    });
}