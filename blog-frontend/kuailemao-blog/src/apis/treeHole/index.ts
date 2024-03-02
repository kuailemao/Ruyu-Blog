import http from "@/utils/http.ts";

// 添加树洞
export const addTreeHole = (content: String) => {
    return http.post("/treeHole/auth/addTreeHole", JSON.stringify({content}), {
        method: 'post'
    });
};

// 获取树洞列表
export const getTreeHoleList = () => {
    return http.get("/treeHole/getTreeHoleList", {
        method: 'get'
    });
};