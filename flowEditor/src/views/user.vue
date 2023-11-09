
<template>
    <div style="height: calc(100vh)">

        <div v-if="!helloVisible">

            <h2 class="ef-tooltar">用户登录</h2>
            <!--  
                <el-input v-model="$store.state.workflow.nodeList[1].requester" style="width: 300px;">用户id</el-input>
                <input type="text" placeholder="用户id" ref="userId">
            -->

            <input type="text" v-model="userId" placeholder="用户id">
            <button @click="login">登录</button>

        </div>



        <div v-if="helloVisible">
            <h2 class="ef-tooltar">欢迎你！{{ userId }}</h2>

            <div>
                <h3 class="ef-node-form-header">待处理任务：</h3>
                <div class="ef-node-form-body" style="margin-left: 10px;">
                    {{ taskList }}
                    <button @click="pass">通过申请</button>
                    <button @click="pass">驳回申请</button>
                </div>
            </div>


            <h3 class="ef-node-form-header">请假：</h3>
            <div class="ef-node-form-body" style="margin-left: 10px;">
                <input type="text" v-model="leaveDays" placeholder="申请天数">
                <button @click="request">发起申请</button>
            </div>

 
            <h3 class="ef-node-form-header">申请结果：</h3>
            <div class="ef-node-form-body" style="margin-left: 10px;">
                <div style="white-space: pre-wrap;">{{ result }}</div>
            </div>

        </div>


        <button v-if="helloVisible" @click="logout" style="margin-left: 10px;margin-top: 50px;">退出登录</button>

    </div>
</template>

<script>
import lodash from 'lodash'


export default {
    data() {
        return {
            // 数据
            userId: "",
            leaveDays: "",
            taskList: "",
            result: "",
            helloVisible: false,
            data: {},

        }
    },


    mounted() {

        //window.console.log(this.$refs.userId.value)
        this.$nextTick(() => {

            // 默认加载流程A
            this.dataReload(this.$store.state.workflow)
            //console.log(this.$store.state.workflow)
        })
    },


    methods: {


        // 加载流程图数据
        dataReload(data) {
            this.easyFlowVisible = false
            this.data.nodeList = []
            this.data.lineList = []
            this.$nextTick(() => {
                data = lodash.cloneDeep(data)
                this.easyFlowVisible = true
                this.data = data
            })
        },


        //login
        login() {

            this.hello()

            //显示任务列表
            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.assignee === this.userId && node.requester != null) {
                    this.taskList = "申请人 [ " + node.requester + " ] 申请请假 [ " + node.leaveDays + " ] 天。"

                }
            })

            //显示申请结果
            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.requester === this.userId && node.assignee != null && node.state === "success") {
                    this.result = this.result + node.assignee + " 已经通过您的申请！" + '\n'
                }
            })

        },

        logout() {

            this.closehello()
            this.taskList = ""
            this.userId = ""
            this.leaveDays = ""
            this.result = ""

        },

        //欢迎banner
        hello() {

            this.helloVisible = true

        },
        closehello() {

            this.helloVisible = false

        },

        //申请
        request() {

            var srcNodeId;
            var destNodeId;
            //console.log(this.userId)
            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.type === "application task") {
                    srcNodeId = node.id
                    node.state = "success"
                    node.isCurrentNode = "NO"
                    node.requester = this.userId
                    node.leaveDays = this.leaveDays
                }
            })

            this.$store.state.workflow.lineList.filter((line) => {
                if (line.from === srcNodeId) {
                    destNodeId = line.to
                    // console.log(destNodeId)
                }
            })

            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.id === destNodeId) {
                    node.state = "running"
                    node.isCurrentNode = "YES"
                    node.requester = this.userId
                    node.leaveDays = this.leaveDays
                }
            })

            this.saveNodeState()
            this.saveLineState()

        },


        //通过申请
        pass() {
            var srcNodeId;
            var destNodeId;
            var requester_t;
            var leaveDays_t;
            //console.log(this.userId)
            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.assignee === this.userId) {
                    srcNodeId = node.id
                    node.state = "success"
                    node.isCurrentNode = "NO"
                    requester_t = node.requester
                    leaveDays_t = node.leaveDays
                }
            })

            this.$store.state.workflow.lineList.filter((line) => {
                if (line.from === srcNodeId) {
                    if (line.label != null) {
                        if (Number(leaveDays_t) > Number(line.greater) && Number(leaveDays_t) < Number(line.less)) {
                            destNodeId = line.to
                        }
                    } else {

                        destNodeId = line.to
                    }
                    // console.log(destNodeId)
                }
            })

            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.id === destNodeId) {
                    node.state = "running"
                    node.isCurrentNode = "YES"
                    node.requester = requester_t
                    node.leaveDays = leaveDays_t

                }
            })


            this.saveNodeState()
            this.saveLineState()


        },

        //驳回申请
        reject() {
            var srcNodeId;
            var destNodeId;

            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.assignee === this.userId) {
                    destNodeId = node.id
                    node.state = "idle"
                    node.isCurrentNode = "NO"
                }
            })

            this.$store.state.workflow.lineList.filter((line) => {
                if (line.to === destNodeId) {
                    srcNodeId = line.from
                    //console.log(srcNodeId)
                }
            })

            this.$store.state.workflow.nodeList.filter((node) => {
                if (node.id === srcNodeId) {
                    node.state = "running"
                    node.isCurrentNode = "YES"
                }
            })


        },


        //数据持久化
        saveNodeState() {

            this.$store.commit('workflow/changeNodeState', this.data.nodeList)

        },

        saveLineState() {

            this.$store.commit('workflow/changeLineState', this.data.lineList)

        },




    }
}
</script>
