<template>
    <el-dialog
            title="流程数据信息"
            :visible.sync="dialogVisible"
            width="70%"
    >

        <!-- codemirror 是一个高亮显示的插件
        插件教程：https://cloud.tencent.com/developer/article/1760809
        -->
        <codemirror
                :value="flowJsonData"
                :options="options"
                class="code"
        ></codemirror>
        
    </el-dialog>
</template>

<script>
    import 'codemirror/lib/codemirror.css'
    import { codemirror } from 'vue-codemirror'


    export default {
        props: {
            data: Object,
        },
        
        data() {
            return {
                dialogVisible: false,
                //flowJsonData: {},
                options: {
                    mode: {name: "javascript", json: true},
                    lineNumbers: true                         //左侧是否有数字显示行数
                }
            }
        },

        components: {
            codemirror
        },


        methods: {
            //将JavaScript转为JSON
            init() {
                this.dialogVisible = true
                this.flowJsonData = JSON.stringify(this.data, null, 4).toString()
            }
        }
    }
</script>
