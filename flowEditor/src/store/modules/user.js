

export default {
    namespaced: true,
    state: () => ({
        counter:100,
        students:[
            {id:1,name:"wuyong",age:22},
            {id:2,name:"wuqi",age:22},
            {id:3,name:"wuman",age:26},
            {id:4,name:"zhanfyongfang",age:55},
            {id:5,name:"wulun",age:55},
        ]
    }),
    mutations: {
        // 改数据函数
        increment(state){
            state.counter++
        },
        decrement(state){
            state.counter--
        },

        insert(state){
            var length = state.students.length+1;
            let newList = {
                id:length,
                name:'hux',
                age:'21',
              }
            state.students.push(newList)
        }


    },
    actions: {
        // 请求数据函数
    },
    getters:{

        more20stu(state){
            return state.students.filter(s=>s.age>20)
        }
    },
}