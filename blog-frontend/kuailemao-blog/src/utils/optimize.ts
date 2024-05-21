/**
 * 节流
 * @param fn 方法回调
 * @param wait 间隔 毫秒
 */
function throttle(fn: Function, wait: number) {
    let lastTime = 0;
    return function() {
        const now = new Date().getTime();
        if (now - lastTime >= wait) {
            fn();
            lastTime = now;
        }
    }
}

export { throttle }