import { reqMusicDetail, reqMusicDescription, reqMusicLyricById } from "@/api/music";

export const MODELLIST = [
  "RANDOM", // 随机
  "LISTLOOP", // 列表循环
  "SINGLECYCLE", // 单曲循环
];

export const PLAYTYPE = {
  CUSTOM: "CUSTOM", // 播放用户添加的歌曲
  TOP: "TOP", // 当前歌曲排行榜列表歌曲
};

/*
 * @author: Zhang Yuming
 * @date: 2023-07-07 16:53:05
 * @params: index 下标 len 歌曲数组长度
 * @description: 随机播放返回不重复的下标
 */
const returnRandomNoRepeat = (index, len) => {
  if (len == 1) {
    return index;
  }
  let res = Math.floor(Math.random() * (len - 0) + 0);

  if (res == index) {
    returnRandomNoRepeat(index);
  } else {
    return res;
  }
};
/*
 * @author: Zhang Yuming
 * @date: 2023-07-04 10:09:57
 * @params: len 歌曲列表长度 index 当前歌曲下标 playType 播放模式 isPlayNext 是下一首还是上一首
 * @description: 返回下一首播放的歌曲
 */
export function getNextMusic(len, index, playType, isPlayNext) {
  let newIndex = 0;
  switch (playType) {
    // 随机
    case "RANDOM":
      newIndex = returnRandomNoRepeat(index, len);
      if (newIndex == index) {
        newIndex = returnRandomNoRepeat(index, len);
      }
      break;
    // 列表循环
    case "LISTLOOP":
      if (isPlayNext) {
        if (index == len - 1) {
          newIndex = 0;
        } else if (index != -1) {
          newIndex = index + 1;
        } else {
          newIndex = 0;
        }
      } else {
        if (index == 0) {
          newIndex = len - 1;
        } else if (index != -1) {
          newIndex = index - 1;
        } else {
          newIndex = 0;
        }
      }
      break;
    // 单曲循环
    case "SINGLECYCLE":
      newIndex = index;
      break;
  }

  return newIndex;
}

/**
 * 给小于10的数字 + 0
 * @param {*} time
 * @returns time
 */
export function addZero(time) {
  if (time >= 0 && time < 10) {
    time = "0" + time;
  }
  return time;
}

/*
 * @author: Zhang Yuming
 * @date: 2023-06-26 10:25:43
 * @params: time 时间 s
 * @description: 返回歌曲时间
 */
export function calcMusicTime(time) {
  // 这里就按照分和秒来
  let minutes = 0,
    second = 0;

  if (!time) {
    return `${addZero(minutes)}:${addZero(second)}`;
  }

  minutes = Math.floor(time / 60);
  second = Math.floor(time % 60);
  return `${addZero(minutes)}:${addZero(second)}`;
}

/*
 * @author: Zhang Yuming
 * @date: 2023-06-26 16:26:53
 * @params: curent 播放到哪儿了 durattion 总时长
 * @description: 计算歌曲播放百分比
 */
export function calcMusicSchedule(current, duration) {
  return Math.round((current / duration) * 10000) / 100;
}

/*
 * @author: Zhang Yuming
 * @date: 2023-10-23 13:33:46
 * @params: progress 进度 duration 播放时长
 * @description: 通过进度计算播放的时长
 */
export function calcMusicCurrentTime(progress, duration) {
  return Math.round(progress * duration) / 100;
}

/**
 * 根据歌曲id获取歌曲详情
 * @param {*} id
 */
export const getMusicDetail = async (id) => {
  const res = await reqMusicDetail({
    id,
    level: "exhigh",
  });
  if (res.code == 200) {
    // 设置音乐详情 播放器通过监听音乐的id 进行音乐播放
    return res.data[0];
  }
};

export const getMusicDescription = async (id) => {
  const res = await reqMusicDescription(id);
  if (res.code == 200) {
    // 设置音乐详情 播放器通过监听音乐的id 进行音乐播放
    return res.songs;
  }
};

export const getLyric = async (id) => {
  const res = await reqMusicLyricById(id);
  if (res.code == 200) {
    let lyricArr = res.lrc.lyric.split("\n");
    const notNullLyricArr = [];
    const timeList = [];
    lyricArr.forEach((v) => {
      let arr = v.split("]");
      let timeArr = arr[0].replace("[", "").split(":");
      if (arr[1] && arr[0]) {
        // 不为空才收集歌词
        timeList.push((timeArr[0] - 0) * 1000 * 60 + (timeArr[1] - 0) * 1000);
        notNullLyricArr.push(arr[1]);
      }
    });
    return {
      lyricList: notNullLyricArr, // 歌词列表
      lyricTimeList: timeList, // 歌词时间列表
    };
  }
};
