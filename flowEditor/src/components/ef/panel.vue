<template>
    <div v-if="easyFlowVisible" style="height: calc(100vh);">
        <el-row>
            <!--顶部工具菜单-->
            <el-col :span="24">
                <div class="ef-tooltar" style="width: 100%;">
                    <el-button @click="dataInfo" size="mini">流程信息</el-button>
                    <el-button @click="nodeInfo" size="mini">编辑节点</el-button>
                    <el-button @click="downloadData" size="mini">下载流程JSON</el-button>

                    <div style="float: right;margin-right: 10px">

                    </div>

                </div>
            </el-col>
        </el-row>


        <div style="display: flex;height: calc(100% - 47px);">

            <!-- 左侧菜单 -->
            <div style="width: 230px;border-right: 1px ;">
                <node-menu @addNode="addNode" ref="nodeMenu"></node-menu>

            </div>


            <!-- 根据data里面的nodeList数组画图 -->
            <div id="efContainer" ref="efContainer" class="container" v-flowDrag>

                <div style="float: right;margin-right: 10px;margin-top: 10px;">
                    <el-button @click="dataReloadDefault" icon="el-icon-refresh" size="mini">切换空流程</el-button>
                    <el-button @click="dataReloadA" icon="el-icon-refresh" size="mini">切换流程A</el-button>
                </div>

                <div style="float: left;margin-left: 10px;margin-top: 10px;">
                    <el-button @click="startFlow" icon="el-icon-video-pause" size="large">开始流程</el-button>
                </div>

                <template v-for="node in data.nodeList">
                    <flow-node :id="node.id" :node="node" :activeElement="activeElement" @changeNodeSite="changeNodeSite"
                        @nodeRightMenu="nodeRightMenu" @clickNode="clickNode">
                    </flow-node>

                </template>
                <!-- 给画布一个默认的宽度和高度 -->
                <div style="position:absolute;top: 2000px;left: 2000px;">&nbsp;</div>

            </div>

            <!-- 右侧表单 -->
            <div v-if="nodeInfoVisible" style="width: 300px;border-left: 1px;background-color: #FBFBFB">

                <el-button style="background-color: white;color: rgb(255, 90, 90);text-align: center;"
                    @click="deleteElement" :disabled="!this.activeElement.type" size="mini">删除该对象</el-button>

                <flow-node-form ref="nodeForm" @setLineLabel="setLineLabel"
                    @repaintEverything="repaintEverything"></flow-node-form>


            </div>

        </div>


        <!-- 流程信息窗口 -->
        <flow-info v-if="flowInfoVisible" ref="flowInfo" :data="data"></flow-info>

    </div>
</template>

<script>
import draggable from 'vuedraggable'
// import { jsPlumb } from 'jsplumb'
// 使用修改后的jsplumb
import './jsplumb'
import { easyFlowMixin } from '@/components/ef/mixins'
import flowNode from '@/components/ef/node'
import nodeMenu from '@/components/ef/node_menu'
import FlowInfo from '@/components/ef/info'

import FlowNodeForm from './node_form'
import lodash from 'lodash'
import { getDataA } from './data_A'
import { getData } from './data'


var index = 0


export default {

    data() {
        return {
            // jsPlumb 实例
            jsPlumb: null,
            // 控制画布销毁
            easyFlowVisible: true,
            // 控制节点数据的显示与隐藏
            nodeInfoVisible: false,
            // 控制流程数据显示与隐藏
            flowInfoVisible: false,
            // 是否加载完毕标志位
            loadEasyFlowFinish: false,
            // 数据
            data: {},
            // 激活的元素、可能是节点、可能是连线
            activeElement: {
                // 可选值 node 、line
                type: undefined,
                // 节点ID
                nodeId: undefined,
                // 连线ID
                sourceId: undefined,
                targetId: undefined
            },
        }
    },

    // 一些基础配置移动该文件中
    mixins: [easyFlowMixin],

    components: {
        draggable, flowNode, nodeMenu, FlowInfo, FlowNodeForm
    },

    directives: {
        'flowDrag': {
            bind(el, binding, vnode, oldNode) {
                if (!binding) {
                    return
                }
                el.onmousedown = (e) => {
                    if (e.button == 2) {
                        // 右键不管
                        return
                    }
                    //  鼠标按下，计算当前原始距离可视区的高度
                    let disX = e.clientX
                    let disY = e.clientY
                    el.style.cursor = 'move'

                    document.onmousemove = function (e) {
                        // 移动时禁止默认事件
                        e.preventDefault()
                        const left = e.clientX - disX
                        disX = e.clientX
                        el.scrollLeft += -left

                        const top = e.clientY - disY
                        disY = e.clientY
                        el.scrollTop += -top
                    }

                    document.onmouseup = function (e) {
                        el.style.cursor = 'auto'
                        document.onmousemove = null
                        document.onmouseup = null
                    }
                }
            }
        }
    },

    mounted() {
        this.jsPlumb = jsPlumb.getInstance()
        this.$nextTick(() => {

            // 默认加载流程A
            this.dataReload(getDataA())
        })
    },


    methods: {

        // 返回唯一标识，从0开始计数
        getUUID() {
            return 'NODE' + index++
        },


        jsPlumbInit() {
            this.jsPlumb.ready(() => {
                // 导入默认配置
                this.jsPlumb.importDefaults(this.jsplumbSetting)
                // 会使整个jsPlumb立即重绘。
                this.jsPlumb.setSuspendDrawing(false, true);
                // 初始化节点
                this.loadEasyFlow()
                // 单点击了连接线, https://www.cnblogs.com/ysx215/p/7615677.html
                this.jsPlumb.bind('click', (conn, originalEvent) => {
                    this.activeElement.type = 'line'
                    this.activeElement.sourceId = conn.sourceId
                    this.activeElement.targetId = conn.targetId
                    this.$refs.nodeForm.lineInit({
                        from: conn.sourceId,
                        to: conn.targetId,
                        label: conn.getLabel()
                    })
                })
                // 连线
                this.jsPlumb.bind("connection", (evt) => {
                    let from = evt.source.id
                    let to = evt.target.id
                    if (this.loadEasyFlowFinish) {
                        this.data.lineList.push({ from: from, to: to })
                    }
                })

                // 删除连线回调
                this.jsPlumb.bind("connectionDetached", (evt) => {
                    this.deleteLine(evt.sourceId, evt.targetId)
                })

                // 改变线的连接节点
                this.jsPlumb.bind("connectionMoved", (evt) => {
                    this.changeLine(evt.originalSourceId, evt.originalTargetId)
                })

                // 连线右击
                this.jsPlumb.bind("contextmenu", (evt) => {
                    console.log('contextmenu', evt)
                })

                // 连线动作合法性判断
                this.jsPlumb.bind("beforeDrop", (evt) => {
                    let from = evt.sourceId
                    let to = evt.targetId
                    if (from === to) {

                        return false
                    }
                    if (this.hasLine(from, to)) {

                        return false
                    }
                    if (this.hashOppositeLine(from, to)) {

                        return false
                    }

                    return true
                })

                // beforeDetach
                this.jsPlumb.bind("beforeDetach", (evt) => {
                    console.log('beforeDetach', evt)
                })
                this.jsPlumb.setContainer(this.$refs.efContainer)
            })
        },

        // 加载流程图
        loadEasyFlow() {
            // 初始化节点
            for (var i = 0; i < this.data.nodeList.length; i++) {
                let node = this.data.nodeList[i]
                // 设置源点，可以拖出线连接其他节点
                this.jsPlumb.makeSource(node.id, lodash.merge(this.jsplumbSourceOptions, {}))
                // // 设置目标点，其他源点拖出的线可以连接该节点
                this.jsPlumb.makeTarget(node.id, this.jsplumbTargetOptions)
                if (!node.viewOnly) {
                    this.jsPlumb.draggable(node.id, {
                        containment: 'parent',
                        stop: function (el) {
                            console.log('拖拽结束: ', el)
                        }
                    })
                }
            }
            // 初始化连线
            for (var i = 0; i < this.data.lineList.length; i++) {
                let line = this.data.lineList[i]
                var connParam = {
                    source: line.from,
                    target: line.to,
                    label: line.label ? line.label : '',
                    connector: line.connector ? line.connector : '',
                    anchors: line.anchors ? line.anchors : undefined,
                    paintStyle: line.paintStyle ? line.paintStyle : undefined,
                }
                this.jsPlumb.connect(connParam, this.jsplumbConnectOptions)
            }
            this.$nextTick(function () {
                this.loadEasyFlowFinish = true
            })
        },

        // 设置连线条件
        setLineLabel(from, to, label,greater,less) {
            var conn = this.jsPlumb.getConnections({
                source: from,
                target: to
            })[0]
            if (!label || label === '') {
                conn.removeClass('flowLabel')
                conn.addClass('emptyFlowLabel')
            } else {
                conn.addClass('flowLabel')
            }
            conn.setLabel({
                label: label,
                greater: greater,
                less: less
            })
            this.data.lineList.forEach(function (line) {
                if (line.from == from && line.to == to) {
                    line.label = label
                    line.greater = greater
                    line.less = less
                }
            })

        },

        // 删除激活的元素
        deleteElement() {
            if (this.activeElement.type === 'node') {
                this.deleteNode(this.activeElement.nodeId)
            } else if (this.activeElement.type === 'line') {

                var conn = this.jsPlumb.getConnections({
                    source: this.activeElement.sourceId,
                    target: this.activeElement.targetId
                })[0]
                this.jsPlumb.deleteConnection(conn)

            }
        },

        // 删除线
        deleteLine(from, to) {
            this.data.lineList = this.data.lineList.filter(function (line) {
                if (line.from == from && line.to == to) {
                    return false
                }
                return true
            })
        },

        // 改变连线
        changeLine(oldFrom, oldTo) {
            this.deleteLine(oldFrom, oldTo)
        },

        // 改变节点的位置
        changeNodeSite(data) {
            for (var i = 0; i < this.data.nodeList.length; i++) {
                let node = this.data.nodeList[i]
                if (node.id === data.nodeId) {
                    node.left = data.left
                    node.top = data.top
                }
            }
        },

        /* 拖拽结束后添加新的节点
            * @param evt
            * @param nodeMenu 被添加的节点对象
            * @param mousePosition 鼠标拖拽结束的坐标
        */
        addNode(evt, nodeMenu, mousePosition) {
            var screenX = evt.originalEvent.clientX, screenY = evt.originalEvent.clientY
            let efContainer = this.$refs.efContainer
            var containerRect = efContainer.getBoundingClientRect()
            var left = screenX, top = screenY

            left = left - containerRect.x + efContainer.scrollLeft
            top = top - containerRect.y + efContainer.scrollTop
            // 居中
            left -= 85
            top -= 16
            var nodeId = this.getUUID()

            //初始化节点名称与节点默认属性
            var origName = nodeMenu.name
            var nodeName = origName
            var type = nodeMenu.type
            var node = {
                id: nodeId,
                name: nodeName,
                type: type,
                left: left + 'px',
                top: top + 'px',
                state: 'idle'
            }
            if (node.type === "begin") {
                node.isCurrentNode = "YES"
            } else {
                node.isCurrentNode = "NO"
            }


            /**
             * 这里可以进行业务判断、是否能够添加该节点
             */
            this.data.nodeList.push(node)
            this.$nextTick(function () {
                this.jsPlumb.makeSource(nodeId, this.jsplumbSourceOptions)
                this.jsPlumb.makeTarget(nodeId, this.jsplumbTargetOptions)
                this.jsPlumb.draggable(nodeId, {
                    containment: 'parent',
                    stop: function (el) {
                        // 拖拽节点结束后的对调
                        console.log('拖拽结束: ', el)
                    }
                })
            })
        },


        /* 删除节点
            * @param nodeId 被删除节点的ID
        */
        deleteNode(nodeId) {
            // 这里需要进行业务判断，是否可以删除
            this.data.nodeList = this.data.nodeList.filter(function (node) {
                if (node.id === nodeId) {
                    return false
                }
                return true
            })
            this.$nextTick(function () {
                this.jsPlumb.removeAllEndpoints(nodeId);
            })
            return true
        },

        //激活点击对象节点
        clickNode(nodeId) {
            //右侧信息窗口可视化以后才可以激活节点
            this.activeElement.type = 'node'
            this.activeElement.nodeId = nodeId
            this.$refs.nodeForm.nodeInit(this.data, nodeId)
        },

        // 是否具有该线
        hasLine(from, to) {
            for (var i = 0; i < this.data.lineList.length; i++) {
                var line = this.data.lineList[i]
                if (line.from === from && line.to === to) {
                    return true
                }
            }
            return false
        },

        // 是否含有相反的线
        hashOppositeLine(from, to) {
            return this.hasLine(to, from)
        },

        nodeRightMenu(nodeId, evt) {
            this.menu.show = true
            this.menu.curNodeId = nodeId
            this.menu.left = evt.x + 'px'
            this.menu.top = evt.y + 'px'
        },

        repaintEverything() {
            this.jsPlumb.repaint()
        },

        // 节点数据信息显示状态切换
        nodeInfo() {
            this.nodeInfoVisible = !this.nodeInfoVisible

        },


        // 流程数据信息
        dataInfo() {
            this.flowInfoVisible = true
            this.$nextTick(function () {
                //获取流程图json格式的数据，此方法定义在info.vue中
                this.$refs.flowInfo.init()
            })
        },

        // 加载流程图
        dataReload(data) {
            this.easyFlowVisible = false
            this.data.nodeList = []
            this.data.lineList = []
            this.$nextTick(() => {
                data = lodash.cloneDeep(data)
                this.easyFlowVisible = true
                this.data = data
                this.$nextTick(() => {
                    this.jsPlumb = jsPlumb.getInstance()
                    this.$nextTick(() => {
                        this.jsPlumbInit()
                    })
                })
            })
        },


        // 载入数据data
        dataReloadDefault() {
            this.dataReload(getData())
        },
        // 模拟载入数据dataA
        dataReloadA() {
            this.dataReload(getDataA())
        },


        // 下载流程数据
        downloadData() {
            var datastr = "data:text/json;charset=utf-8," + encodeURIComponent(JSON.stringify(this.data, null, '\t'));
            var downloadAnchorNode = document.createElement('a')
            downloadAnchorNode.setAttribute("href", datastr);
            downloadAnchorNode.setAttribute("download", 'data.json')
            downloadAnchorNode.click();
            downloadAnchorNode.remove();
        },


        // 开始流程运行
        startFlow() {
            this.nodeInfoVisible = !this.nodeInfoVisible

            for (var i = 0; i < this.data.nodeList.length; i++) {
                var node = this.data.nodeList[i]
                if (node.type === "begin") {
                    this.data.nodeList[i].state = "success"
                    this.data.nodeList[i].isCurrentNode = "NO"

                    var srcNodeId = node.id
                    var destNodeId
                    for (var j = 0; j < this.data.lineList.length; j++) {
                        if (this.data.lineList[j].from === srcNodeId) {
                            destNodeId = this.data.lineList[j].to
                            //console.log(destNodeId)
                        }
                    }

                    for (var k = 0; k < this.data.nodeList.length; k++) {
                        var node_1 = this.data.nodeList[k]
                        //console.log(this.data.nodeList[k].id)
                        if (node_1.id === destNodeId) {
                            console.log(this.data.nodeList[k].state)
                            this.data.nodeList[k].state = "running"
                            this.data.nodeList[k].isCurrentNode = "YES"
                            break
                        }

                    }
                }

                break
            }
        }




    }
}
</script>
