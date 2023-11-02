<template>
    <div
            ref="node"
            :style="nodeContainerStyle"
            @click="clickNode"
            @mouseup="changeNodeSite"
            :class="nodeContainerClass"
    >

        <!-- 节点名称 -->
        <div class="ef-node-text flow-node-drag" :show-overflow-tooltip="true">
            {{node.name}}
        </div>

        <!-- 节点状态，分为两种状态，1是成功，2是正在运行，3是闲置，使用标签i设置图标 
        标签网站：https://element.eleme.cn/#/zh-CN/component/icon  -->
        <div class="ef-node-right-ico" style="font-size: 20px;">
            <i class="el-icon-circle-check el-node-state-success" v-show="node.state === 'success'"></i>
            <i class="el-icon-loading el-node-state-running" v-show="node.state === 'running'"></i>
            <i class="el-icon-video-play el-node-state-idle" v-show="node.state === 'idle'"></i>
        </div>
        
    </div>
</template>

<script>
    export default {
        props: {
            node: Object,
            activeElement: Object
        },
        data() {
            return {}
        },
        computed: {
            nodeContainerClass() {
                return {
                    'ef-node-container': true,
                    'ef-node-active': this.activeElement.type == 'node' ? this.activeElement.nodeId === this.node.id : false
                }
            },

            // 节点容器样式，返回xy坐标
            nodeContainerStyle() {
                return {
                    top: this.node.top,
                    left: this.node.left
                }
            },

            nodeIcoClass() {
                var nodeIcoClass = {}
                nodeIcoClass[this.node.ico] = true
                // 添加该class可以推拽连线出来，viewOnly 可以控制节点是否运行编辑
                nodeIcoClass['flow-node-drag'] = this.node.viewOnly ? false : true
                return nodeIcoClass
            }
        },
        
        methods: {
            // 点击节点
            clickNode() {
                this.$emit('clickNode', this.node.id)
            },

            // 鼠标移动后抬起
            changeNodeSite() {
                // 避免抖动
                if (this.node.left == this.$refs.node.style.left && this.node.top == this.$refs.node.style.top) {
                    return;
                }
                this.$emit('changeNodeSite', {
                    nodeId: this.node.id,
                    left: this.$refs.node.style.left,
                    top: this.$refs.node.style.top,
                })
            }


        }
    }
</script>
