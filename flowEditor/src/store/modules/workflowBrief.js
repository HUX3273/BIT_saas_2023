export default {
    namespaced: true,
    state: () => ({
        workflowList: [
            //{ flowId: "", requester: "", leaveDays: "" }
        ]
    }),
    mutations: {
        // 改数据函数

        changeState(state,data){
            
        },

        insert(state, data) {
            let newList = {
                flowId: data.flowId,
                requester: data.requester,
                leaveDays: data.leaveDays,
            }
            state.workflowList.push(newList)
        }

    },
    actions: {
        // 请求数据函数
    },
    getters: {


    },
}