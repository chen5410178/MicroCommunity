<template>
    <div class="group-content">
        <el-menu :default-active="currentProjectId" class="group-menu" @select="handleGroupSelect">
            <el-menu-item v-for="item in projectList" :key="item.projectId" :index="item.projectId">
                <span>{{ item.projectName }}</span>
            </el-menu-item>
        </el-menu>
    </div>
</template>
<script>
import { getProjectList } from '@/api/project/projectApi'
export default {
    name: 'ProjectTab',
    data() {
        return {
            projectList: [],
            currentProjectId: ''
        }
    },
    mounted() {
        this.getProjectList()
    },
    methods: {
        handleGroupSelect(projectId) {
            this.currentProjectId = projectId;
            this.$emit('selectProject', projectId)
        },
        getProjectList() {
            getProjectList({
                page: 1,
                row: 1000
            }).then(res => {
                console.log(res)
                this.projectList = res.data
            })
        }
    }
}
</script>
<style scoped>

</style>
