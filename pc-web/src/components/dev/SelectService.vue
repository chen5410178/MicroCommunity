<template>
    <el-dialog :visible.sync="dialogVisible" title="选择接口" width="70%">

        <!-- 新增查询表单 -->
        <el-form :inline="true" :model="searchForm" class="search-form">
            <el-form-item :label="$t('service.table.serviceCode')">
                <el-input v-model="searchForm.serviceCode" :placeholder="$t('service.table.serviceCode')"
                    clearable></el-input>
            </el-form-item>
            <el-form-item :label="$t('service.table.name')">
                <el-input v-model="searchForm.name" :placeholder="$t('service.table.name')" clearable></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </el-form-item>
        </el-form>

        <el-table :data="tableData" border style="width: 100%">
            <el-table-column :label="$t('service.table.serviceCode')" prop="serviceCode"></el-table-column>
            <el-table-column :label="$t('service.table.name')" prop="name"></el-table-column>
            <el-table-column :label="$t('service.table.serviceId')" prop="serviceId"></el-table-column>
            <el-table-column :label="$t('service.table.url')" prop="url"></el-table-column>
            <el-table-column label="操作" width="100">
                <template slot-scope="scope">
                    <el-button @click="handleSelect(scope.row)" size="mini">
                        {{ $t('common.selectBtn') }}
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="pagination-container">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
                :current-page="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                layout="total, sizes, prev, pager, next, jumper" :total="page.total"></el-pagination>
        </div>
    </el-dialog>
</template>

<script>
import { getServiceList } from '@/api/dev/serviceApi'

export default {
    name: 'SelectService',
    data() {
        return {
            page: {
                current: 1,
                size: 10,
                total: 0
            },
            tableData: [],
            dialogVisible: false,
            searchForm: {
                serviceCode: '',
                name: ''
            }
        }
    },
    created() { },
    methods: {
        openDialog() {
            this.dialogVisible = true;
            this._loadApiList();
        },
        async _loadApiList() {
            this.loading = true
            try {
                const params = {
                    page: this.page.current,
                    row: this.page.size,
                    ...this.searchForm  // 将搜索条件合并到请求参数
                }
                const { services, total } = await getServiceList(params);
                this.tableData = services;
                this.page.total = total;
            } catch (error) {
                console.error(error)
            } finally {
                this.loading = false
            }
        },
        handleSelect(row) {
            this.$emit('select', row);
            this.dialogVisible = false;
        },
        handleSizeChange(val) {
            this.page.size = val;
            this._loadApiList();
        },
        handleCurrentChange(val) {
            this.page.current = val;
            this._loadApiList();
        },
        handleSearch() {
            this.page.current = 1; // 搜索时重置页码
            this._loadApiList();
        }
    }
};
</script>

<style scoped>
:deep(.el-dialog__header) {
    text-align: left;
}

:deep(.el-dialog__body) {
    padding-top: 10px
}

.search-form {
    text-align: right;
}
</style>