<template>
    <el-dialog :visible.sync="dialogVisible" title="选择用户" width="40%">
        <el-table :data="userDatas" border style="width: 100%">
            <el-table-column :label="$t('login.username')" prop="name"></el-table-column>
            <el-table-column :label="$t('login.storeName')" prop="storeName"></el-table-column>
            <el-table-column :label="$t('login.relCdName')" prop="relCdName"></el-table-column>
            <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                    <el-button @click="handleSelect(scope.row)" size="mini">
                        {{$t('common.selectBtn')}}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
    </el-dialog>
</template>

<script>

    export default {
        name: 'SelectApp',
        data() {
            return {
              userDatas:[],
              dialogVisible: false
            }
        },
        created() {},
        methods: {
            openDialog(_users) {
                this.userDatas = _users
                this.dialogVisible = true;
            },
            handleSelect(row) {
                this.$emit('select', row);
                this.dialogVisible = false;
            }
        }
    };
</script>

<style scoped>
</style>