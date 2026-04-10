<template>
  <div>
    <div class="flex justify-between">
      <div></div>
      <div v-if="simplifyOwnerMemberInfo.ownerId">
        <el-button type="primary" size="small" @click="openAddMemberModel">
          <i class="el-icon-plus"></i>{{ $t('simplifyOwnerMember.addMember') }}
        </el-button>
      </div>
    </div>

    <el-table
      :data="simplifyOwnerMemberInfo.members"
      style="width: 100%; margin-top: 10px"
      border>
      <el-table-column prop="memberId" :label="$t('simplifyOwnerMember.memberId')" align="center"></el-table-column>
      <el-table-column prop="name" :label="$t('simplifyOwnerMember.name')" align="center"></el-table-column>
      <el-table-column prop="sex" :label="$t('simplifyOwnerMember.gender')" align="center">
        <template #default="{row}">
          {{row.sex == 0 ? $t('simplifyOwnerMember.male') : $t('simplifyOwnerMember.female')}}
        </template>
      </el-table-column>
      <el-table-column prop="ownerTypeName" :label="$t('simplifyOwnerMember.type')" align="center"></el-table-column>
      <el-table-column prop="idCard" :label="$t('simplifyOwnerMember.idCard')" align="center"></el-table-column>
      <el-table-column prop="link" :label="$t('simplifyOwnerMember.contact')" align="center"></el-table-column>
      <el-table-column prop="userName" :label="$t('simplifyOwnerMember.creator')" align="center"></el-table-column>
      <el-table-column prop="remark" :label="$t('simplifyOwnerMember.remark')" align="center"></el-table-column>
      <el-table-column 
        v-for="(item,index) in simplifyOwnerMemberInfo.listColumns" 
        :key="index" 
        :label="item" 
        align="center">
        <template #default="{row}">
          {{row.listValues[index]}}
        </template>
      </el-table-column>
      <el-table-column :label="$t('simplifyOwnerMember.operation')" align="center">
        <template #default="{row}">
          <el-button type="text" @click="_openEditOwnerModel(row)">
            {{ $t('simplifyOwnerMember.edit') }}
          </el-button>
          <el-button type="text" @click="_openDeleteOwnerModel(row)">
            {{ $t('simplifyOwnerMember.delete') }}
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <el-pagination
      @current-change="handleCurrentChange"
      :current-page="currentPage"
      :page-size="pageSize"
      layout="total, prev, pager, next"
      :total="total">
    </el-pagination>

    <add-owner-member ref="addOwnerMember" @success="listSimplifyOwnerMember"></add-owner-member>
    <edit-owner-member ref="editOwnerMember" @success="listSimplifyOwnerMember"></edit-owner-member>
    <delete-owner ref="deleteOwner" @success="listSimplifyOwnerMember"></delete-owner>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwnerMembers } from '@/api/simplify/simplifyOwnerMemberApi'
import AddOwnerMember from '@/components/owner/addOwnerMember'
import EditOwnerMember from '@/components/owner/editOwnerMember'
import DeleteOwner from '@/components/owner/deleteOwner'

export default {
  name: 'SimplifyOwnerMember',
  components: {
    AddOwnerMember,
    EditOwnerMember,
    DeleteOwner
  },
  data() {
    return {
      simplifyOwnerMemberInfo: {
        members: [],
        ownerId: '',
        total: 0,
        records: 1,
        listColumns: []
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.getSimplifyOwnerMemberColumns()

  },
  methods: {
    open(param) {
      this.handleSwitch(param)
    },
    handleSwitch(param) {
      if (param.ownerId == '') return
      this.clearSimplifyOwnerMemberInfo()
      Object.assign(this.simplifyOwnerMemberInfo, param)
      this.listSimplifyOwnerMember(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.listSimplifyOwnerMember(val, this.pageSize)
    },
    listSimplifyOwnerMember(page, rows) {
      const params = {
        page,
        row: rows,
        ownerId: this.simplifyOwnerMemberInfo.ownerId,
        communityId: this.communityId
      }
      
      queryOwnerMembers(params).then(res => {
        this.simplifyOwnerMemberInfo.members = res.data
        this.dealSimplifyOwnerMemberAttr(res)
        this.total = res.total
      })
    },
    openAddMemberModel() {
      this.$refs.addOwnerMember.open({ownerId: this.simplifyOwnerMemberInfo.ownerId})
    },
    _openDeleteOwnerModel(member) {
      member.ownerId = this.simplifyOwnerMemberInfo.ownerId
      this.$refs.deleteOwner.open(member)
    },
    _openEditOwnerModel(member) {
      member.ownerId = this.simplifyOwnerMemberInfo.ownerId
      this.$refs.editOwnerMember.open(member)
    },
    clearSimplifyOwnerMemberInfo() {
      const listColumns = this.simplifyOwnerMemberInfo.listColumns
      this.simplifyOwnerMemberInfo = {
        members: [],
        ownerId: '',
        listColumns
      }
    },
    dealSimplifyOwnerMemberAttr(owners) {
      owners.forEach(item => {
        this.getSimplifyOwnerMemberColumnsValue(item)
      })
    },
    getSimplifyOwnerMemberColumnsValue(owner) {
      owner.listValues = []
      if (!owner.ownerAttrDtos || owner.ownerAttrDtos.length < 1) {
        this.simplifyOwnerMemberInfo.listColumns.forEach(() => {
          owner.listValues.push('')
        })
        return
      }
      
      this.simplifyOwnerMemberInfo.listColumns.forEach(value => {
        let tmpValue = ''
        owner.ownerAttrDtos.forEach(attrItem => {
          if (value == attrItem.specName) {
            tmpValue = attrItem.valueName
          }
        })
        owner.listValues.push(tmpValue)
      })
    },
    getSimplifyOwnerMemberColumns() {
      this.simplifyOwnerMemberInfo.listColumns = []
      // Implement get attr spec logic
    }
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 10px;
}
</style>