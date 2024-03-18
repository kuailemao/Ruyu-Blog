import http from "@/utils/http.ts";

/** 获取榜单 */
export const reqToplist = () => {
    return new Promise((resolve, reject) => {
        http.get("/wapi/toplist/detail", {}).then((res) => {
            resolve(res);
        });
    });
};

/** 获取榜单歌曲列表 */
export const reqTopDetaliList = ({ id, limit, offset }) => {
    return new Promise((resolve, reject) => {
        http
            .get(`/wapi/playlist/track/all?id=${id}&limit=${limit}&offset=${offset}`, {})
            .then((res) => {
                resolve(res);
            });
    });
};

/** 获取歌曲详情 主要是播放地址 */
export const reqMusicDetail = ({ id, level }) => {
    return new Promise((resolve, reject) => {
        http.get(`/wapi/song/url/v1?id=${id}&level=${level}`, {}).then((res) => {
            resolve(res);
        });
    });
};

// 获取音乐的描述
export const reqMusicDescription = (id) => {
    return new Promise((resolve, reject) => {
        http.get(`/wapi//song/detail?ids=${id}`, {}).then((res) => {
            resolve(res);
        });
    });
};

// 搜索
export const reqSearch = (keyWords) => {
    return new Promise((resolve, reject) => {
        http.get(`/wapi/search/suggest?keywords=${keyWords}`, {}).then((res) => {
            resolve(res);
        });
    });
};
// 根据歌手搜索热门歌曲
export const reqSearchSingerHot = ({ id, limit, offset }) => {
    return new Promise((resolve, reject) => {
        http
            .get(`/wapi/artist/top/song?id=${id}&offset=${offset}&limit=${limit}&order=hot`, {})
            .then((res) => {
                resolve(res);
            });
    });
};
// 根据歌曲id获取歌词
export const reqMusicLyricById = (id) => {
    return new Promise((resolve, reject) => {
        http.get(`/wapi/lyric?id=${id}`, {}).then((res) => {
            resolve(res);
        });
    });
};
