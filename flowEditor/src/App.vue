<template>
    <div id="app">
        <router-view></router-view>
        <div>
            <button @click="changeView1">to_home</button>
            <button @click="changeView2">to_about</button>
            <button @click="goToUser">goToUser</button>
            <button @click="goToFlow">goToFlow</button>


            <h2>{{ $store.state.user.counter }}</h2>
            <button @click=" $store.commit('user/increment')">+</button>
            <button @click="$store.commit('user/decrement')">-</button>
            

            <h2>--------------getters先关信息内容--------------</h2>
            <button @click="$store.commit('user/insert')">插入user</button>
            
            <h2>{{ $store.getters['user/more20stu'] }}</h2>
            

            <h2>{{ $store.getters['workflow/showFlowName'] }}</h2>
            <h2>{{ $store.getters['workflow/showAllNode'] }}</h2>
            <h2>{{ $store.getters['workflow/showAllLine'] }}</h2>


        </div>

    </div>
</template>

<script>
import FlowPanel from '@/components/ef/panel'
import axios from 'axios'


export default {
    name: 'App',
    components: {
        FlowPanel
    },
    created() {
        console.log("app组件被创建了！")
        axios.get("http://localhost:8088/workflow").then((response) => {
            console.log(response.data[0])
            console.log("已经取到数据！")
        
            this.$store.commit('workflow/changeNodeState', response.data[0].nodelist)
            this.$store.commit('workflow/changeLineState', response.data[0].linelist)
            this.$store.commit('workflow/changeFlowId',response.data[0].flowid)
            this.$store.commit('workflow/changeRequester',response.data[0].requester)
            this.$store.commit('workflow/changeFlowName',response.data[0].flowname)
        })
    },
    methods: {


        addition() {
            this.$store.commit('user/increment')
        },
        subtraction() {
            this.$store.commit('user/decrement')
        },





        changeView1: function () {
            this.$router.push({
                name: "home",
            });
        },
        changeView2: function () {
            this.$router.push({
                name: "about",
            });
        },
        goToUser: function () {
            this.$router.push({
                name: "user",
            });
        },
        goToFlow: function () {
            this.$router.push({
                name: "flow",
            });
        },
    }


}
</script>
