import { getDataA } from '../../components/ef/data_A'

export default {
    namespaced: true,
    state: () => ({
        flowId:"",
        flowname:"",
        requester:"",
        nodeList: [],
        lineList: [],
    }),

    mutations: {
        // 改数据函数

        changeNodeState(state,nodeList){
            state.nodeList = nodeList;
        },

        changeLineState(state,lineList){
            state.lineList = lineList;
        },

        changeFlowId(state,flowId){
            state.flowId = flowId;
        },

        changeRequester(state,requester){
            state.requester = requester;
        },

        changeFlowName(state,flowname){
            state.flowname = flowname;
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