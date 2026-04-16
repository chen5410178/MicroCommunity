<template>
    <el-dialog :visible.sync="dialogVisible" title="选择API" width="70%">
        <el-table :data="tableData" border style="width: 100%">
            <el-table-column :label="$t('app.table.name')" prop="name"></el-table-column>
            <el-table-column :label="$t('app.table.appId')" prop="appId"></el-table-column>
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
    import { getAppList } from '@/api/dev/appApi'

    export default {
        name: 'SelectApp',
        data() {
            return {
                page: {
                    current: 1,
                    size: 10,
                    total: 0
                },
                tableData: [],
                dialogVisible: false
            }
        },
        created() {},
        methods: {
            openDialog() {
                this.dialogVisible = true;
                this._loadApiList();
            },
            async _loadApiList() {
                const params = {
                    page: this.page.current,
                    row: this.page.size,
                }
                const { data,total } = await getAppList(params);
                this.tableData = data;
                this.page.total = total;
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