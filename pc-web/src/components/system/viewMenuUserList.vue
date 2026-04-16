<template>
    <div class="view-menu-user-container">
        <el-dialog :visible.sync="dialogVisible" width="80%" :before-close="handleClose"
            custom-class="view-menu-user-dialog">
            <el-row>
                <el-col :span="22"></el-col>
                <el-col :span="2" class="text-right">
                    <i class="el-icon-close close-icon" @click="handleClose"></i>
                </el-col>
            </el-row>

            <el-row class="menu-items" :gutter="20">
                <el-col v-for="(item, index) in viewMenuUserInfo.menus" :key="index" :span="4"
                    class="text-center menu-item" @click.native="jumpToUserPage(item)">
                    <div>
                        <i :class="item.icon" class="menu-icon"></i>
                    </div>
                    <div class="menu-name">
                        {{ item.name }}
                    </div>
                </el-col>
            </el-row>

            <el-row class="menu-items" :gutter="20">
                <el-col :span="4" class="text-center menu-item" @click.native="toUserMenu">
                    <div>
                        <i class="el-icon-plus menu-icon"></i>
                    </div>
                    <div class="menu-name">
                        {{ $t('common.add') }}
                    </div>
                </el-col>
            </el-row>
        </el-dialog>
    </div>
</template>

<script>
import { listMenuUser } from '@/api/system/viewMenuUserApi'

export default {
    name: 'ViewMenuUserList',
    data() {
        return {
            dialogVisible: false,
            viewMenuUserInfo: {
                menus: []
            }
        }
    },
    methods: {
        open() {
            this.dialogVisible = true
            this.listViewMenuUsers()
        },
        handleClose() {
            this.dialogVisible = false
        },
        async listViewMenuUsers() {
            try {
                const params = {
                    page: 1,
                    row: 100
                }
                const { data } = await listMenuUser(params)
                this.viewMenuUserInfo.menus = data
            } catch (error) {
                console.error('Failed to fetch menu data:', error)
            }
        },
        toUserMenu() {
            this.handleClose()
            this.$router.push('/views/system/menuUserManage?tab=commonMenu')
        },
        jumpToUserPage(item) {
            this.handleClose()
            let href = item.url
            if (href.indexOf('/#/') > -1) {
                href = href.replace('/#/', '/')
            }
            if (href.indexOf('?') > -1) {
                href += `&tab=${item.name}`
            } else {
                href += `?tab=${item.name}`
            }
            this.$router.push(href)
        }
    }
}
</script>

<style lang="scss" scoped>
.view-menu-user-container {
    ::v-deep .view-menu-user-dialog {
        background: rgba(66, 66, 66, 0.9);
        color: #fff;
        box-shadow: 0 0px 0px rgba(0, 0, 0, 0.3);

        .el-dialog__header {
            display: none;
        }

        .el-dialog__body {
            color: #fff;
            padding: 20px;
        }
    }

    .close-icon {
        font-size: 18px;
        cursor: pointer;

        &:hover {
            color: #409EFF;
        }
    }

    .menu-items {
        margin-top: 40px;
        cursor: pointer;
    }

    .menu-item {
        margin-bottom: 20px;

        &:hover {

            .menu-icon,
            .menu-name {
                color: #409EFF;
            }
        }
    }

    .menu-icon {
        font-size: 48px;
        margin-bottom: 10px;
    }

    .menu-name {
        font-size: 14px;
    }
}
</style>