<!-- views/staff/staffList.vue -->
<template>
    <div class="staff-container">
        <!-- Search Conditions -->
        <div class="search-wrapper">
            <el-card>
                <div slot="header" class="clearfix">
                    <span>{{ $t('staff.searchConditions') }}</span>
                </div>
                <el-row :gutter="20">
                    <el-col :span="6">
                        <el-input v-model="staffInfo.conditions.name" :placeholder="$t('staff.staffName')" clearable />
                    </el-col>
                    <el-col :span="6">
                        <el-input v-model="staffInfo.conditions.tel" :placeholder="$t('staff.phoneNumber')" clearable />
                    </el-col>
                    <el-col :span="6">
                        <el-input v-model="staffInfo.conditions.staffId" :placeholder="$t('staff.staffId')" clearable />
                    </el-col>
                    <el-col :span="6">
                        <el-button type="primary" @click="_queryStaffMethod">
                            <i class="el-icon-search"></i>
                            {{ $t('common.search') }}
                        </el-button>
                        <el-button @click="_resetStaffMethod">
                            <i class="el-icon-refresh"></i>
                            {{ $t('common.reset') }}
                        </el-button>
                    </el-col>
                </el-row>
            </el-card>
        </div>

        <!-- Staff List -->
        <div class="list-wrapper">
            <el-card>
                <div slot="header" class="clearfix">
                    <span>{{ $t('staff.staffManagement') }}</span>
                    <el-button type="primary" size="small" style="float: right;" @click="_openAddStaffStepPage">
                        <i class="el-icon-plus"></i>
                        {{ $t('staff.add') }}
                    </el-button>
                </div>

                <el-table v-loading="loading" :data="staffData" border style="width: 100%">
                    <el-table-column prop="userId" :label="$t('staff.staffNumber')" align="center" />
                    <el-table-column prop="name" :label="$t('staff.name')" align="center" />
                    <el-table-column prop="tel" :label="$t('staff.phone')" align="center" />
                    <el-table-column prop="orgName" :label="$t('staff.relatedOrg')" align="center" />
                    <el-table-column prop="email" :label="$t('staff.email')" align="center" />
                    <el-table-column prop="address" :label="$t('staff.address')" align="center" />
                    <el-table-column prop="relCdName" :label="$t('staff.relCd')" align="center" />
                    <el-table-column prop="sex" :label="$t('staff.gender')" align="center">
                        <template slot-scope="scope">
                            {{ scope.row.sex == 0 ? $t('staff.male') : $t('staff.female') }}
                        </template>
                    </el-table-column>
                    <el-table-column :label="$t('staff.operations')" align="center" width="300">
                        <template slot-scope="scope">
                            <el-button size="mini" @click="openEditStaff(scope.row)">
                                {{ $t('staff.edit') }}
                            </el-button>
                            <el-button size="mini" @click="_resetStaffPwd(scope.row)">
                                {{ $t('staff.resetPassword') }}
                            </el-button>
                            <el-button size="mini" type="danger" @click="openDeleteStaff(scope.row)">
                                {{ $t('staff.delete') }}
                            </el-button>
                            <el-button size="mini" @click="openStaffDetail(scope.row)">
                                {{ $t('staff.details') }}
                            </el-button>
                        </template>
                    </el-table-column>
                </el-table>

                <div class="tips margin-top-xs">
                    {{ $t('staff.tips') }}
                </div>

                <el-pagination :current-page.sync="page.current" :page-sizes="[10, 20, 30, 50]" :page-size="page.size"
                    :total="page.total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
                    @current-change="handleCurrentChange" />
            </el-card>
        </div>

        <reset-staff-pwd :visible.sync="resetPwdVisible" :staff-info="currentStaff" @success="handleSuccess" />

        <edit-staff ref="editStaff" :staff-info="currentStaff" @success="handleSuccess" />

        <delete-staff ref="deleteStaff" @success="handleSuccess" />
    </div>
</template>

<script>
import { queryStaffInfos, listOrgs } from '@/api/staff/staffApi'
import ResetStaffPwd from '@/components/staff/resetStaffPwd'
import EditStaff from '@/components/staff/editStaff'
import DeleteStaff from '@/components/staff/deleteStaff'

export default {
    name: 'StaffList',
    components: {
        ResetStaffPwd,
        EditStaff,
        DeleteStaff
    },
    data() {
        return {
            loading: false,
            staffInfo: {
                moreCondition: false,
                branchOrgs: [],
                departmentOrgs: [],
                relCds: [],
                conditions: {
                    branchOrgId: '',
                    departmentOrgId: '',
                    orgId: '',
                    orgName: '',
                    orgLevel: '2',
                    parentOrgId: '',
                    name: '',
                    tel: '',
                    staffId: '',
                    parentOrgName: '',
                    parentTwoOrgId: ''
                }
            },
            staffData: [],
            page: {
                current: 1,
                size: 10,
                total: 0
            },
            resetPwdVisible: false,
            editStaffVisible: false,
            deleteStaffVisible: false,
            currentStaff: {}
        }
    },
    watch: {
        'staffInfo.conditions.branchOrgId': {
            handler(val) {
                this._getOrgsByOrgLevelStaff(1, 10, 3, val)
                this.staffInfo.conditions.parentOrgId = val
                this.staffInfo.conditions.departmentOrgId = ''
                this.loadData(1, 10)
            },
            deep: true
        },
        'staffInfo.conditions.departmentOrgId': {
            handler(val) {
                this.staffInfo.conditions.orgId = val
                this.loadData(1, 10)
            },
            deep: true
        }
    },
    created() {
        this.loadData(1, 10)
        //this._getOrgsByOrgLevelStaff(1, 10, 2, '')
    },
    methods: {
        async loadData(page, size) {
            try {
                this.loading = true
                const params = {
                    page: page || this.page.current,
                    row: size || this.page.size,
                    ...this.staffInfo.conditions
                }

                const { staffs, total } = await queryStaffInfos(params)
                this.staffData = staffs
                this.page.total = total
            } catch (error) {
                this.$message.error(error.message)
            } finally {
                this.loading = false
            }
        },
        openEditStaff(staff) {
            this.currentStaff = { ...staff }
            this.editStaffVisible = true
            this.$refs.editStaff.open(staff)
        },
        openDeleteStaff(staff) {
            this.currentStaff = { ...staff }
            this.$refs.deleteStaff.open(staff)
        },
        _moreCondition() {
            this.staffInfo.moreCondition = !this.staffInfo.moreCondition
        },
        async _getOrgsByOrgLevelStaff(page, size, orgLevel, parentOrgId) {
            try {
                const params = {
                    page,
                    row: size,
                    orgLevel,
                    parentOrgId
                }

                const { orgs } = await listOrgs(params)
                if (orgLevel === 2) {
                    this.staffInfo.branchOrgs = orgs
                } else {
                    this.staffInfo.departmentOrgs = orgs
                }
            } catch (error) {
                this.$message.error(error.message)
            }
        },
        _openAddStaffStepPage() {
            this.$router.push('/views/staff/addStaff')
        },
        _queryStaffMethod() {
            this.loadData(1, this.page.size)
        },
        _resetStaffMethod() {
            this.staffInfo.conditions = {
                branchOrgId: '',
                orgId: '',
                departmentOrgId: '',
                name: '',
                tel: '',
                staffId: ''
            }
            this.loadData(1, this.page.size)
        },
        _resetStaffPwd(staff) {
            this.currentStaff = { ...staff }
            this.resetPwdVisible = true
        },
        openStaffDetail(staff) {
            this.$router.push(`/views/staff/staffDetail?staffId=${staff.userId}`)
        },
        handleSuccess() {
            this.loadData(this.page.current, this.page.size)
        },
        handleSizeChange(val) {
            this.page.size = val
            this.loadData(1, val)
        },
        handleCurrentChange(val) {
            this.page.current = val
            this.loadData(val, this.page.size)
        }
    }
}
</script>

<style lang="scss" scoped>
.staff-container {
    padding: 20px;

    .search-wrapper {
        margin-bottom: 20px;
        text-align: left;

        .el-card__header {
            padding: 10px 20px;
            font-weight: bold;

        }
    }

    .list-wrapper {
        text-align: left;

        .el-card__header {
            padding: 10px 20px;
            font-weight: bold;
        }

        .tips {
            margin-top: 20px;
            padding: 10px;
            background-color: #f8f8f8;
            border-radius: 4px;
            color: #666;
            font-size: 14px;
        }

        .el-pagination {
            margin-top: 20px;
            text-align: right;
        }
    }

    .margin-top-xs {
        margin-top: 10px;
    }
}
</style>