export default {
    namespaced: true,
    state: () => ({
        id: '',
        nickname: '',
        avatar: '',
        token: '',
        mobile: ''
    }),
    mutations: {
        // 改数据函数
        setUser(state, payload) {
            state.nickname = 'zhangsan'
        }

    },
    actions: {
        // 请求数据函数
    }
}