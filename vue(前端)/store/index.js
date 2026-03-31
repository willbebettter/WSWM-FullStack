import {defineStore} from "pinia";
import {ref} from "vue";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import {createPinia} from "pinia"

// 创建并导出pinia实例
export const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

export const pi = defineStore(
    "store", () => {
        const token = ref("")
        const tokenset = (tok) => {
            token.value = tok
        }
        const tokenclear = () => {
            token.value = ""
        }
        return {
            token,
            tokenset,
            tokenclear
        }
    }, {
        persist: {
            key: "token",
            storage: localStorage,
            paths: ["token"]
        }
    })

