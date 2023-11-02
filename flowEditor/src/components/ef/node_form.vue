<template>
    <div>
        <div class="ef-node-form">
            <div class="ef-node-form-header">
                编辑
                <el-button style="float: right;margin-right: 10px;color:green;" @click="save">保存编辑</el-button>
            </div>
            <div class="ef-node-form-body">

                <!--  节点信息显示与编辑    -->
                <el-form :model="node" ref="dataForm" v-show="type === 'node'">
                    <el-form-item label="类型">
                        <el-input v-model="node.type" :disabled="true"></el-input>
                        <el-select v-model="node.type" placeholder="请选择">
                            <el-option v-for="item in typeList" :key="item.type" :label="item.label" :value="item.type">
                            </el-option>
                        </el-select>
                    </el-form-item>

                    <el-form-item label="名称">
                        <el-input v-model="node.name"></el-input>
                    </el-form-item>

                    <el-form-item label="是否为当前任务节点">
                        <el-input v-model="node.isCurrentNode" :disabled="true"></el-input>
                    </el-form-item>

                    <el-form-item label="申请人">
                        <el-input v-model="node.requester">
                        </el-input>
                        <el-form-item label="请假天数">
                            <el-input v-model="node.leaveDays"></el-input>
                            <el-button @click="request">发起申请</el-button>
                        </el-form-item>

                    </el-form-item>

                    <el-form-item label="处理人">
                        <el-input v-model="node.assignee"></el-input>
                        <el-button @click="approval">通过申请</el-button>
                        <el-button style="color: red;" @click="reject">驳回申请</el-button>
                    </el-form-item>

                    <el-form-item label="left坐标">
                        <el-input v-model="node.left" :disabled="true"></el-input>
                    </el-form-item>

                    <el-form-item label="top坐标">
                        <el-input v-model="node.top" :disabled="true"></el-input>
                    </el-form-item>

                    <el-form-item label="状态">
                        <el-input v-model="node.state" :disabled="true"></el-input>
                    </el-form-item>



                </el-form>


                <!--  连线的编辑    -->
                <el-form :model="line" ref="dataForm" v-show="type === 'line'">
                    <el-form-item label="条件标签">
                        <el-input v-model="line.label"></el-input>
                    </el-form-item>
                    <el-form-item label="大于">
                        <el-input v-model="line.greater"></el-input>
                    </el-form-item>
                    <el-form-item label="小于">
                        <el-input v-model="line.less"></el-input>
                    </el-form-item>

                    <el-form-item>
                        <el-button @click="saveLine">保存</el-button>
                    </el-form-item>
                </el-form>


            </div>
        </div>
    </div>
</template>

<script>
import { cloneDeep } from 'lodash'

export default {
    data() {
        return {
            visible: true,
            // node 或 line
            type: 'node',
            node: {},
            line: {},
            data: {},
            typeList: [{
                type: 'application task',
                label: '申请任务'
            }, {
                type: 'approval task',
                label: '审批任务'
            }]
        }
    },

    methods: {
        /**
         * 表单修改，这里可以根据传入的ID进行业务信息获取
         * @param data
         * @param id
         */
        nodeInit(data, id) {
            this.type = 'node'
            this.data = data
            data.nodeList.filter((node) => {
                if (node.id === id) {
                    this.node = cloneDeep(node)
                }
            })
        },
        lineInit(line) {
            this.type = 'line'
            this.line = line
        },


        //审批
        //发起申请
        request() {
            var srcNodeId = this.node.id;
            var destNodeId;
            if (this.node.isCurrentNode === "YES" && this.node.type === "application task") {

                this.data.nodeList.filter((node) => {
                    if (node.id === this.node.id) {
                        node.name = this.node.name
                        node.type = this.node.type
                        node.left = this.node.left
                        node.top = this.node.top
                        node.state = "success"
                        node.isCurrentNode = "NO"
                        node.requester = this.node.requester
                        node.assignee = this.node.assignee
                        node.leaveDays = this.node.leaveDays
                    }
                })

                this.data.lineList.filter((line) => {
                    if (line.from === srcNodeId) {
                        destNodeId = line.to
                        // console.log(destNodeId)
                    }
                })

                this.data.nodeList.filter((node) => {
                    if (node.id === destNodeId) {
                        node.state = "running"
                        node.isCurrentNode = "YES"
                        node.requester = this.node.requester
                        node.leaveDays = this.node.leaveDays
                        this.$emit('repaintEverything')
                    }
                })
            }
        },
        //通过申请
        approval() {
            var srcNodeId = this.node.id;
            var destNodeId;
            if (this.node.isCurrentNode === "YES" && this.node.type === "approval task") {

                this.data.nodeList.filter((node) => {
                    if (node.id === this.node.id) {
                        node.name = this.node.name
                        node.type = this.node.type
                        node.left = this.node.left
                        node.top = this.node.top
                        node.state = "success"
                        node.isCurrentNode = "NO"
                        node.requester = this.node.requester
                        node.assignee = this.node.assignee
                        node.leaveDays = this.node.leaveDays
                        //console.log(Number(node.leaveDays))
                    }
                })

                this.data.lineList.filter((line) => {
                    if (line.from === srcNodeId) {
                        if (line.label != null) {
                            if (Number(this.node.leaveDays) > Number(line.greater) && Number(this.node.leaveDays) < Number(line.less)) {
                                destNodeId = line.to
                            }
                        } else {

                            destNodeId = line.to
                        }
                        // console.log(destNodeId)
                    }
                })



                this.data.nodeList.filter((node) => {
                    if (node.id === destNodeId) {
                        node.state = "running"
                        node.isCurrentNode = "YES"
                        node.requester = this.node.requester
                        node.leaveDays = this.node.leaveDays
                        this.$emit('repaintEverything')
                    }
                })
            }




        },
        //驳回申请
        reject() {
            var srcNodeId;
            var destNodeId = this.node.id;
            if (this.node.isCurrentNode === "YES" && this.node.type === "approval task") {
                this.data.nodeList.filter((node) => {
                    if (node.id === this.node.id) {
                        node.name = this.node.name
                        node.type = this.node.type
                        node.left = this.node.left
                        node.top = this.node.top
                        node.state = "idle"
                        node.isCurrentNode = "NO"
                        node.requester = this.node.requester
                        node.assignee = this.node.assignee
                        node.leaveDays = this.node.leaveDays
                    }
                })

                this.data.lineList.filter((line) => {
                    if (line.to === destNodeId) {
                        srcNodeId = line.from
                        console.log(srcNodeId)
                    }
                })

                this.data.nodeList.filter((node) => {
                    if (node.id === srcNodeId) {
                        node.state = "running"
                        node.isCurrentNode = "YES"
                        this.$emit('repaintEverything')
                    }
                })
            }

        },


        // 保存修改连线
        saveLine() {
            this.$emit('setLineLabel', this.line.from, this.line.to, this.line.label, this.line.greater, this.line.less)
        },
        // 保存编辑的节点信息
        save() {
            this.data.nodeList.filter((node) => {
                if (node.id === this.node.id) {
                    node.name = this.node.name
                    node.type = this.node.type
                    node.left = this.node.left
                    node.top = this.node.top
                    node.requester = this.node.requester
                    node.assignee = this.node.assignee
                    node.leaveDays = this.node.leaveDays

                    this.$emit('repaintEverything')
                }
            })
        }
    }
}
</script>
