import { loadingImg } from "@/utils/base64-img/loading-img.ts";

// 图片懒加载自定义指令
const lazyBinding = (el: any, binding: any) => {
    // 如果已经加载过，则直接返回，不做处理
    if (el.dataset.lazyLoaded) return;

    const placehold = loadingImg;
    const { nolazy } = binding.value || {};

    // 如果设置了nolazy，直接加载真实图片，并打上标记
    if (nolazy) {
        el.src = el.dataset.src || placehold;
        el.dataset.lazyLoaded = "true";
        return;
    }

    // 设置占位图
    el.src = placehold;

    const observer = new IntersectionObserver((entries) => {
        if (entries.find(v => v.intersectionRatio > 0)) {
            el.src = el.dataset.src || placehold;
            observer.unobserve(el);
            // 标记图片已经加载过
            el.dataset.lazyLoaded = "true";
        }
    });

    observer.observe(el);
};

export default {
    beforeMount(el: HTMLElement, binding: any) {
        lazyBinding(el, binding);
    },
    updated(el: HTMLElement, binding: any) {
        // updated 钩子中，只有在数据变化时才调用
        lazyBinding(el, binding);
    },
};