import { getDataA } from '../../components/ef/data_A'

export default {
    namespaced: true,
    state: () => getDataA(),

    mutations: {
        // 改数据函数
        increment(state){
            state.counter++
        },
        decrement(state){
            state.counter--
        },

        changeNodeState(state,nodeList){
            state.nodeList = nodeList;
        },

        changeLineState(state,lineList){
            state.lineList = lineList;
        },
        

       

    },
    actions: {
        // 请求数据函数
    },
    getters:{


        showFlowName(state){
            return state.name
        },

        showAllNode(state){
            return state.nodeList
        },

        showAllLine(state){
            return state.lineList
        },

        
        
        


    },
}