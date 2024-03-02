import {defineStore} from 'pinia';

const useLoadingStore = defineStore('loading', () => {
    const isLoading = shallowRef<boolean>(false)

    function hide(){
        document.body.style.overflow = '';
        isLoading.value = false
    }
    function show(){
        document.body.style.overflow = 'hidden';
        isLoading.value = true
    }

    return {
        isLoading,
        hide,
        show
    }
})

export default useLoadingStore;
