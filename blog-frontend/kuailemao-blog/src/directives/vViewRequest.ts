import {onUnmounted} from 'vue';

const vViewport = {
    mounted(el: HTMLElement, binding: any) {
        const options = {
            root: binding.value?.root || null, // 可选的根元素，默认为视口
            rootMargin: binding.value?.rootMargin || '0px', // 可选的根元素边距
            threshold: binding.value?.threshold || 0, // 可选的相交比例阈值
        };

        const observer = new IntersectionObserver((entries) => {
            entries.forEach((entry) => {
                if (entry.isIntersecting) {
                    // 元素进入视口，执行加载操作
                    binding.value?.callback?.(el);
                    // 停止观察，避免重复触发
                    observer.unobserve(el);
                }
            });
        }, options);

        observer.observe(el);

        // 在组件卸载时停止观察
        onUnmounted(() => {
            observer.unobserve(el);
        });
    },
};

export default vViewport;