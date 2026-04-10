<template>
    <div class="apiType-container">
        <!-- 查询条件 -->
        <div class="search-wrapper">
            <div class="search-title">{{ $t('apiType.search.title') }}</div>
            <div class="search-content">
                <el-input
                    v-model="searchForm.typeName"
                    :placeholder="$t('apiType.search.typeName')"
                    class="search-item"
                    clearable
                    @keyup.enter.native="handleSearch"
                />
                <el-input
                    v-model="searchForm.createTime"
                    :placeholder="$t('apiType.search.createTime')"
                    class="search-item"
                    clearable
                    @keyup.enter.native="handleSearch"
                />
                <el-button type="primary" @click="handleSearch">{{ $t('common.search') }}</el-button>
            </div>
        </div>

        <div class="list-wrapper">
            <div class="list-header">
                <div class="list-title">{{ $t('apiType.list.title') }}</div>
                <el-button type="primary" @click="handleAdd">{{ $t('common.add') }}</el-button>
            </div>

            <el-table
                v-loading="loading"
                :data="tableData"
                border
                style="width: 100%"
            >
                <el-table-column
                    prop="typeId"
                    :label="$t('apiType.table.typeId')"
                />
                <el-table-column
                    prop="typeName"
                    :label="$t('apiType.table.typeName')"
                />
                <el-table-column
                    prop="createTime"
                    :label="$t('apiType.table.createTime')"
                />
                <el-table-column
                    :label="$t('common.operation')"
                    width="200"
                    fixed="right"
                >
                    <template slot-scope="scope">
                        <el-button
                            size="mini"
                            type="primary"
                            @click="handleEdit(scope.row)"
                        >{{ $t('common.edit') }}</el-button>
                        <el-button
                            size="mini"
                            type="danger"
                            @click="handleDelete(scope.row)"
                        >{{ $t('common.delete') }}</el-button>
                    </template>
                </el-table-column>
            </el-table>

            <el-pagination
                :current-page.sync="page.current"
                :page-sizes="[10, 20, 30, 50]"
                :page-size="page.size"
                :total="page.total"
                layout="total, sizes, prev, pager, next, jumper"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
            />
        </div>
        <AddApiType
            :visible.sync="addVisible"
            @success="handleSuccess"
        />

        <EditApiType
            :visible.sync="editVisible"
            :form-data="currentRow"
            @success="handleSuccess"
        />

        <DelApiType
            :visible.sync="delVisible"
            :type-id="currentRow.typeId"
            @success="handleSuccess"
        />
    </div>
</template>

<script>
import { getApiTypeList } from '@/api/tenant/apiTypeApi'
import AddApiType from '@/components/tenant/AddApiType'
import EditApiType from '@/components/tenant/EditApiType'
import DelApiType from '@/components/tenant/DelApiType'

export default {
    name: 'apiTypeList',
    components: {
        AddApiType,
        EditApiType,
        DelApiType
    },
    data() {
        return {
            loading: false,
            searchForm: {
                typeName: '',
            },
            tableData: [],
            page: {
                current: 1,
                size: 10,
                total: 0
            },
            addVisible: false,
            editVisible: false,
            delVisible: false,
            currentRow: {}
        }
    },
    created() {
        this.getList()
    },
    methods: {
        async getList() {
            try {
                this.loading = true
                const params = {
                    page: this.page.current,
                    row: this.page.size,
                    typeName: this.searchForm.typeName,
                    createTime: this.searchForm.createTime,
                }
                const { data } = await getApiTypeList(params)
                this.tableData = data.data
                this.page.total = data.total
            } catch (error) {
                this.$message.error(this.$t('apiType.fetchError'))
            } finally {
                this.loading = false
            }
        },
        handleSearch() {
            this.page.current = 1
            this.getList()
        },
        handleAdd() {
            this.addVisible = true
        },
        handleEdit(row) {
            this.currentRow = { ...row }
            this.editVisible = true
        },
        handleDelete(row) {
            this.currentRow = { ...row }
            this.delVisible = true
        },
        handleSuccess() {
            this.getList()
        },
        handleSizeChange(val) {
            this.page.size = val
            this.getList()
        },
        handleCurrentChange(val) {
            this.page.current = val
            this.getList()
        }
    }
}
</script>

<style lang="scss" scoped>
.apiType-container {
    padding: 0;
    margin: 0;

    .search-wrapper {
        background-color: #fff;
        padding: 20px;
        margin-bottom: 20px;
        border-radius: 4px;

        .search-title {
            font-size: 16px;
            font-weight: bold;
            margin-bottom: 20px;
            text-align: left;
            color: #333;
        }

        .search-content {
            display: flex;
            align-items: center;
            gap: 20px;

            .search-item {
                width: 200px;
            }
        }
    }

    .list-wrapper {
        background-color: #fff;
        padding: 20px;
        border-radius: 4px;

        .list-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 20px;

            .list-title {
                font-size: 16px;
                font-weight: bold;
                color: #333;
            }
        }

        .el-pagination {
            margin-top: 20px;
            text-align: right;
        }
    }
}
</style>