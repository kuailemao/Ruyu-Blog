export const constantRouter = [
    {
        path: '/',
        name: 'layout',
        component: () => import('@/views/Layout/index.vue'),
        children: [
            // 首页
            {
                path: '',
                component: () => import('@/views/Home/index.vue'),
                name: 'home',
            },
            // 时间轴
            {
                path: '/timeline',
                component: () => import('@/views/Pigeonhole/TimeLine/index.vue'),
                name: 'timeline',
            },
            // 分类
            {
                path: '/category/:id?',
                component: () => import('@/views/Pigeonhole/Category/index.vue'),
                name: 'category',
            },
            // 标签
            {
                path: '/tags/:id?',
                component: () => import('@/views/Pigeonhole/Tags/index.vue'),
                name: 'tags',
            },
            // 树洞
            {
                path: '/tree-hole',
                component: () => import('@/views/Amusement/TreeHole/index.vue'),
                name: 'treeHole',
            },
            // 留言版
            {
                path: '/message',
                component: () => import('@/views/Amusement/Message/index.vue'),
                name: 'message',
                children: [
                    {
                        path: '',
                        component: () => import('@/views/Amusement/Message/MessageList/index.vue'),
                        name: 'messageList',
                    },
                    {
                        path: '/message/detail/:id?',
                        component: () => import('@/views/Amusement/Message/MessageDetail/index.vue'),
                        name: 'messageDetail',
                    }
                ]
            },
            {
                path: '/chat-gpt',
                component: () => import('@/views/ChatGpt/index.vue'),
                name: 'chat-gpt',
            },
            // 友链
            {
                path: '/link',
                component: () => import('@/views/Link/index.vue'),
                name: 'link',
            },
            // 关于
            {
                path: '/about',
                component: () => import('@/views/About/index.vue'),
                name: 'about',
            }
        ]
    },
    // 文章
    {
        path: '/article/:id',
        component:
            () => import('@/views/Article/index.vue'),
        name: 'article',
    },
    // 登录
    {
        path: '/welcome',
        component: () => import('@/views/Welcome/index.vue'),
        name: 'welcome',
        redirect: '/login',
        children: [
            {
                path: '/login',
                component: () => import('@/views/Welcome/Login/index.vue'),
                name: 'welcome-login',
            },
            {
                path: '/register',
                component: () => import('@/views/Welcome/Register/index.vue'),
                name: 'welcome-register',
            },
            {
                path: '/reset',
                component: () => import('@/views/Welcome/Reset/index.vue'),
                name: 'welcome-reset',
            }
        ]
    },
    // 访问其他任何不存在的路由，重定向到首页
    {
        path: '/:pathMatch(.*)*',
        redirect: '/',
        name: 'any',
    }
]