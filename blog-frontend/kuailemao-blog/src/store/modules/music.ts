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
                id: "1403318151", // 正在播放歌曲的id
                url: "http://m701.music.126.net/20240315174154/b729698101072dfd65b5799467c1254a/jdymusic/obj/wo3DlMOGwrbDjj7DisKw/14096426425/36e7/1b7a/21db/c3aa221bd5b7c204455a1f65c65633ee.mp3", // 正在播放音乐的详情 音乐地址
                lyricList: [
                    " 作词 : 童子-T/Shingo.S", " 作曲 : 童子-T", " 编曲 : 闫津", " 制作人 : 王佳依", "采样曲：願い~あの頃のキミへ~", "中文填词：一只然", "OP（原属词曲版权公司）：テレビ東京ミュージック 东京电视台音乐", "本作品经过原词曲作者以及版权公司授权", "我们之间的回忆", "全部都小心地收集", "我总是偷偷地哭泣", "像倦鸟失了归期", "但愿我相信的爱情", "结局紧握在我手心", "时光匆匆却没有遗失过去", "希望我们 有光明的未来", "还有能够装下星空的期待", "可现实为何让我感到如此懈怠", "总怀念相遇时我们无视落叶和人海", "是你让我勇敢不再像颗尘埃", "是你常帮我照料装着梦的盆栽", "每一天我们都是如此愉快", "一直到天色渐晚看着落日无奈离开", "我知道你爱集邮爱笑甚至爱发呆", "我知道你怕草虫还有夜晚的妖怪", "我喜欢你有一点心不在焉的状态", "看起来像个回到七岁时候的小孩", "该如何将我这份感情向你告白", "喜欢却又不敢爱", "直到整个宇宙", "在为我焦虑失神慌张之中醒来", "就像是黑暗过后黎明盛开", "我们之间的回忆", "全部都小心地收集", "我总是偷偷地哭泣", "像倦鸟失了归期", "但愿我相信的爱情", "结局紧握在我手心", "时光匆匆却没有遗失过去", "那天你在雨后街角答应接受我的爱", "那一刻我的世界有了色彩", "这一生无法忘记关于澄蓝色的你", "像一份礼物悄然呈现在我的境遇", "我们从清晨起玩一整天游戏", "到夜晚一起看我最爱的剧", "能够拥有这些已足够幸运", "我已经不再期待其他什么东西", "我们也经常争执互相不接电话", "在同一房间终于呼叫冷漠抵达", "也曾想过以后生活没有人牵挂", "把爱扔掉只剩一块残酷伤疤", "而那些你送我的梦，教我折的花", "逃亡的盛夏", "在夜空隐没之际为我点燃烟花", "寂寞时让我把快乐拼起来吧", "我们之间的回忆", "全部都小心地收集", "我总是偷偷地哭泣", "像倦鸟失了归期", "但愿我相信的爱情", "结局紧握在我手心", "时光匆匆却没有遗失过去", "我们可以就像是孩子一样成长", "永远无所畏惧义无反顾向着未知的前方", "原来我们每天努力长成看似大人的模样", "为了可以一起跨越山和海洋", "那时的我每天都祈祷 实现这个梦想", "现在的你还好吗", "是否还会像从前一样的爱笑", "现在的你还好吗", "当你径直在我面前坦言放下", "现在的你还好吗", "是否像从前一样有无限的温柔啊", "现在的你还好吗", "愿你能保持月亮般的心，要爱自己啊", "我们之间的回忆", "全部都小心地收集", "我总是偷偷地哭泣", "像倦鸟失了归期", "但愿我相信的爱情", "结局紧握在我手心", "时光匆匆却没有遗失过去", "独自收集两个人之间的回忆", "即使每当到这时候我都会哭泣", "为何一切，变得如此，无法回到过去", "但我仍愿意感谢你给过我爱情", "每一场风景都是我们爱的证明", "就算如今，天各一方，祝你余生动听啊", "独自收集两个人之间的回忆", "即使每当到这时候我都会哭泣", "为何一切，变得如此，无法回到过去", "但我仍愿意感谢你给过我爱情", "每一场风景都是我们爱的证明", "就算如今，天各一方，把回忆拼好给你", "企划：谢宇航 / 牛雪吟 / 裘小静", "制作人：王佳依", "监制：姚政", "录音：陈程", "缩混：陈程", "封面设计：刘馨宇", "网易云音乐特别企划“听，海风”", "本歌曲来自〖网易音乐人〗", "10亿现金激励，千亿流量扶持！", "合作：st399@vip.163.com"
                ], // 歌词列表
                lyricTimeList: [
                    0,530,1060,1590,2120,12150,15230,19490,27800,30550,33670,36560,39870,42520,45300,55250,58020,60927,64049,67658,70378,73250,76048,79649,82390,85349,88530,91640,94119,95690,97569,100330,102747,105470,108390,111470,114289,117280,120319,138738,142389,145330,148367,151217,154367,157378,160210,163669,166449,169360,172187,175669,178818,180277,182987,186508,189367,192307,195318,198288,201229,204229,210867,216490,222639,228729,234080,241818,243848,247419,249729,253318,255628,259449,262420,267800,270278,273290,276278,279577,282119,285059,291920,294629,297610,303987,306559,309660,315898,318600,321819,327800,330629,333610,350499,355040,358379,364509,368379,371350,375648,5940010,5940020,5940029
                ], // 歌词时间列表
            },
            // 正在播放音乐的描述
            musicDescription: {
                al: {
                    picUrl: blogAvatar,
                },
                name: "",
                ar: [
                    {
                        name: "歌手走丢了",
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
