import {defineStore} from "pinia"; //引入pinia
// 歌曲枚举
import {MODELLIST, PLAYTYPE, LYRICTYPE} from "@/utils/enum";
// 歌曲工具
import {
    getNextMusic,
    calcMusicCurrentTime,
    calcMusicSchedule,
    getMusicDetail,
    getLyric,
    getMusicDescription,
} from "@/components/Music/musicTool";

import blogAvatar from "@/assets/images/blogAvatar.svg";

const audio = new Audio();

export const music = defineStore("music", {
    persist: {
        enabled: true, //开启数据持久化
        strategies: [
            {
                key: "musicState", //给一个要保存的名称
                storage: localStorage, //sessionStorage / localStorage 存储方式
            },
        ],
    },
    state: () => {
        return {
            volume: 0.5, // 音量
            isPaused: true, // 音乐播放器是否暂停
            currentTime: 0, // 当前播放的时间
            duration: 0, // 歌曲总时长
            musicInfo: {
                id: "", // 正在播放歌曲的id
                url: "", // 正在播放音乐的详情 音乐地址
                lyricList: [
                    ""
                ], // 歌词列表
                lyricTimeList: [], // 歌词时间列表
            },
            // 正在播放音乐的描述
            musicDescription: {
                al: {
                    picUrl: blogAvatar,
                },
                name: "",
                ar: [
                    {
                        name: "听音乐吧~",
                    },
                ],
            },
            lyricType: LYRICTYPE.COMMON, // 歌词模式
            showLyricBoard: false, // 是否展示歌词板
            currentLyticIndex: 0, // 当前歌词的下标
            isShow: false, // 是否展示音乐控制器
            isToggleImg: false, // 是否正在切换图片
            playType: PLAYTYPE.TOP, // 播放列表 是用户选择的列表还是当前歌曲排行榜的列表 top表示排行 user表示用户选择的
            playModel: MODELLIST[0], // 播放模式 随机：RANDOM 列表循环：LISTLOOP 单曲循环：SINGLECYCLE
            musicList: [], // 当前排行榜音乐列表
            customerMusicList: [], // 用户添加的音乐列表
            currentSchedule: 0,
            isUseProgress: false,
            isClickLyric: false,
        };
    },
    getters: {
        getAudio() {
            return audio;
        },
        getCurrentTime() {
            return this.currentTime;
        },
        getDuration() {
            return this.duration;
        },
        getVolume() {
            return this.volume;
        },
        getIsPaused() {
            return this.isPaused;
        },
        // 获取当前播放进度
        getCurrentSchedule() {
            return this.currentSchedule;
        },
        getMusicDescription() {
            return this.musicDescription;
        },
        getLyricType() {
            return this.lyricType;
        },
        getMusicInfo() {
            return this.musicInfo;
        },
        getMusicList() {
            return this.musicList;
        },
        getShowLyricBoard() {
            return this.showLyricBoard;
        },
        getCurrentLyticIndex() {
            return this.currentLyticIndex;
        },
        getIsShow() {
            return this.isShow;
        },
        getIsToggleImg() {
            return this.isToggleImg;
        },
        getPlayType() {
            return this.playType;
        },
        getPlayModel() {
            return this.playModel;
        },
        getCustomerMusicList() {
            return this.customerMusicList;
        },
    },
    actions: {
        // 初始化音乐播放器
        init() {
            audio.volume = this.volume;
            audio.loop = false;
            audio.autoplay = true;
            audio.preload = true;
            audio.crossOrigin = "anonymous";

            // 随着音乐播放的变化，需要设置 当前时间的变化 歌词变化
            audio.ontimeupdate = () => {
                if (audio.currentTime) {
                    this.currentTime = audio.currentTime;
                }

                if (this.isPaused != audio.paused) {
                    this.isPaused = audio.paused;
                }

                if (this.duration != audio.duration) {
                    this.duration = audio.duration;
                }
                // 设置播放歌词
                if (!this.isClickLyric) {
                    let index = this.musicInfo.lyricTimeList.findIndex((v) => v >= audio.currentTime * 1000);
                    this.currentLyticIndex = index - 1 || 0;
                }

                if (!this.isUseProgress) {
                    this.currentSchedule = calcMusicSchedule(audio.currentTime, audio.duration);
                }
                // 下一首
                if (audio.ended) {
                    this.setNext(true);
                }
            };

            // 初始化的时候如果有音乐id，就获取一下最新的音乐内容
            if (this.musicInfo.id) {
                this.setMusicInfo(this.musicInfo.id, true);
            }
        },
        // 清空当前的时长
        clear() {
            this.duration = 0;
            this.currentLyticIndex = 0;
        },
        // 初始化播放音乐
        setPlay(isInit = false) {
            this.clear();

            // 如果初始化的时候播放进度大于0说明已经播放一段时间了，得自动切换到这歌进度来
            if (isInit) {
                audio.currentTime = this.currentTime;
            } else {
                audio.currentTime = 0;
                this.currentTime = 0;
            }

            // 切换歌曲的时候，让图片回到初始状态
            this.isToggleImg = true;

            if (isInit) {
                if (this.isPaused) {
                    audio.pause();
                } else {
                    audio
                        .play()
                        .then(() => {
                            this.isPaused = false;
                            this.isToggleImg = false;
                        })
                        .catch((res) => {
                            this.isPaused = true;
                            console.log(res);
                        });
                }
            } else {
                audio
                    .play()
                    .then(() => {
                        this.isPaused = false;
                        this.isToggleImg = false;
                    })
                    .catch((res) => {
                        this.isPaused = true;
                        console.log(res);
                    });
            }
        },
        togglePlay() {
            this.isToggleImg = false;
            if (this.isPaused) {
                audio
                    .play()
                    .then(() => {
                        this.isPaused = false;
                    })
                    .catch((err) => {
                        console.log(err);
                    });
            } else {
                audio.pause();
                this.isPaused = true;
            }
        },
        // 设置下一首，或者上一首 ，根据传入参数判断 true 下一首 false 上一首
        setNext(flag = true) {
            let list = this.musicList;
            switch (this.playType) {
                case "TOP":
                    list = this.musicList;
                    break;
                case "CUSTOM":
                    list = this.customerMusicList;
                    break;
                default:
                    break;
            }
            let len = list.length;
            let index = list.findIndex((item) => item.id == this.musicInfo.id);
            if (index == -1) {
                index = 0;
            }
            // 随机/顺序/单曲循环播放的逻辑
            const musicIndex = getNextMusic(len, index, this.playModel, flag);
            this.setMusicInfo(list[musicIndex].id);
        },
        // 设置当前播放音乐的信息 搜索列表的歌曲信息时没有的需要传过来
        async setMusicInfo(id, isInit = false) {
            if (!id) return;
            const des = await getMusicDescription(id);
            // 通过音乐id 获取音乐简介 描述 歌词信息
            if (des) {
                this.setMusicDescription(des[0]);
            }

            // 主要是获取歌曲播放的url地址
            const musicDetail = await getMusicDetail(id);
            const lyric = await getLyric(id);
            let musicInfo = {
                id: id,
                url: musicDetail.url, // 正在播放音乐的详情 音乐地址
                lyricList: lyric.lyricList, // 歌词列表
                lyricTimeList: lyric.lyricTimeList, // 歌词时间列表
            };
            audio.src = musicDetail.url;
            this.musicInfo = musicInfo;

            await this.setPlay(isInit);
        },
        setMusicDescription(val) {
            this.musicDescription = val;
        },
        setMusicList(list) {
            this.musicList = list;
        },
        // 通过用户拉动进度条 切换音乐的播放时间
        setCurrentTime(progress) {
            let time = calcMusicCurrentTime(progress, audio.duration);
            this.currentTime = time;
            audio.currentTime = time;
            // 设置播放歌词
            let index = this.musicInfo.lyricTimeList.findIndex((v) => v >= audio.currentTime * 1000);
            this.currentLyticIndex = index - 1 || 0;

            if (audio.paused) {
                this.togglePlay();
            }
            setTimeout(() => {
                this.isUseProgress = false;
            }, 200);
        },
        // 通过用户点击歌词设置当前播放时间
        setCurrentTimeByClickLyric(index) {
            this.isClickLyric = true;
            let time = this.musicInfo.lyricTimeList[index];
            audio.currentTime = time / 1000;
            this.currentTime = time / 1000;
            if (audio.paused) {
                this.togglePlay();
            }
            setTimeout(() => {
                this.isClickLyric = false;
            }, 100);
        },
        // 设置音量
        setVolume(progress) {
            let volume = Math.round((progress / 100) * 100) / 100;
            this.volume = volume;
            audio.volume = volume;
        },
        setShowLyricBoard(val) {
            this.showLyricBoard = val;
        },
        setIsShow() {
            this.isShow = !this.isShow;
        },
        setCustomerMusicList(type, music) {
            if (type == "add") {
                this.customerMusicList.push(music);
            } else if (type == "delete") {
                let index = this.customerMusicList.findIndex((item) => item.id == music.id);
                if (index != -1) {
                    this.customerMusicList.splice(index, 1);
                }
                if (!this.customerMusicList.length) {
                    this.setPlayType(PLAYTYPE.TOP);
                }
            }
        },
        setIsToggleImg(isToggleImg) {
            this.isToggleImg = isToggleImg;
        },
        setLyricType(val) {
            this.lyricType = LYRICTYPE[val];
        },
        setPlayType(type) {
            this.playType = type;
        },
        setPlayModel(model) {
            this.playModel = model;
        },
        setIsUseProgress(val) {
            this.isUseProgress = val;
        },
    },
});
