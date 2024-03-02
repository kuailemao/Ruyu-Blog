import http from "@/utils/http.ts";

// 所有标签
export function tagList() {
    return http.get("/tag/list", {
        method: "get"
    });
}