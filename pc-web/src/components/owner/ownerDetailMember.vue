<template>
  <div class="margin-top">
    <el-row class="">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input v-model="ownerDetailMemberInfo.name" type="text"
          :placeholder="$t('ownerDetailMember.placeholder.name')" class="form-control"></el-input>
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_qureyOwnerDetailMember()">
          <i class="el-icon-search"></i>{{ $t('common.query') }}
        </el-button>
      </el-col>
      <el-col :span="16" class="text-right">
        <el-button type="primary" size="small" style="margin-left:10px" v-if="hasPrivilege('502023032885101706')"
          @click="openAddMemberModel()">
          <i class="el-icon-plus"></i>{{ $t('common.add') }}
        </el-button>
      </el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="ownerDetailMemberInfo.members" style="width: 100%">
        <el-table-column prop="faceUrl" :label="$t('ownerDetailMember.face')" align="center">
          <template slot-scope="scope">
            <img style="width: 60px; height: 60px;" class="border-radius" v-if="scope.row.faceUrl"
              :src="scope.row.faceUrl" @click="_viewOwnerFace(scope.row.faceUrl)" />
            <img style="width: 60px; height: 60px;" class="border-radius" v-else src="/img/noPhoto.jpg" />
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('ownerDetailMember.name')" align="center"></el-table-column>
        <el-table-column prop="sex" :label="$t('ownerDetailMember.gender')" align="center">
          <template slot-scope="scope">
            {{ scope.row.sex == 0 ? $t('common.male') : $t('common.female') }}
          </template>
        </el-table-column>
        <el-table-column prop="personRoleName" :label="$t('ownerDetailMember.role')" align="center"></el-table-column>
        <el-table-column prop="idCard" :label="$t('ownerDetailMember.idCard')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('ownerDetailMember.contact')" align="center"></el-table-column>
        <el-table-column prop="address" :label="$t('ownerDetailMember.address')" align="center"></el-table-column>
        <el-table-column :label="$t('common.operation')" align="center" width="180">
          <template slot-scope="scope">
            <el-button-group>
              <el-button size="mini" @click="_openEditOwnerModel(scope.row)" v-if="hasPrivilege('502023032899781711')">
                {{ $t('common.edit') }}
              </el-button>
              <el-button size="mini" @click="_openDeleteOwnerModel(scope.row)"
                v-if="hasPrivilege('502023032818941714')">
                {{ $t('common.delete') }}
              </el-button>
            </el-button-group>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination @current-change="handleCurrentChange" :current-page="pagination.currentPage"
        :page-size="pagination.pageSize" :total="pagination.total" layout="total, prev, pager, next, jumper">
      </el-pagination>
    </div>

    <add-owner-member ref="addOwnerMember" @success="_loadOwnerDetailMemberData"></add-owner-member>
    <edit-owner-member ref="editOwnerMember" @success="_loadOwnerDetailMemberData"></edit-owner-member>
    <delete-owner ref="deleteOwner" @refresh="_loadOwnerDetailMemberData" @success="_loadOwnerDetailMemberData"></delete-owner>
  </div>
</template>

<script>
import AddOwnerMember from './addOwnerMember'
import EditOwnerMember from './editOwnerMember'
import DeleteOwner from './deleteOwner'
import { queryOwnerMembers } from '@/api/owner/ownerDetailMemberApi'
import { getCommunityId } from '@/api/community/communityApi'

export default {
  name: 'OwnerDetailMember',
  components: {
    AddOwnerMember,
    EditOwnerMember,
    DeleteOwner
  },
  data() {
    return {
      ownerDetailMemberInfo: {
        members: [],
        ownerId: '',
        name: '',
      },
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  methods: {
    open(ownerId, ownerName, link) {
      this.ownerDetailMemberInfo.ownerId = ownerId
      this.ownerDetailMemberInfo.ownerName = ownerName
      this.ownerDetailMemberInfo.link = link
      this._loadOwnerDetailMemberData(1, this.pagination.pageSize)
    },
    _loadOwnerDetailMemberData(page, row) {
      const params = {
        communityId: getCommunityId(),
        ownerId: this.ownerDetailMemberInfo.ownerId,
        name: this.ownerDetailMemberInfo.name,
        page: page,
        row: row
      }

      queryOwnerMembers(params).then(response => {
        this.ownerDetailMemberInfo.members = response.data
        this.pagination.total = response.total
      }).catch(error => {
        console.error('请求失败处理', error)
      })
    },
    _qureyOwnerDetailMember() {
      this._loadOwnerDetailMemberData(1, this.pagination.pageSize)
    },
    openAddMemberModel() {
      
      this.$refs.addOwnerMember.open({ ownerId: this.ownerDetailMemberInfo.ownerId })
    },
    _openDeleteOwnerModel(member) {
      member.ownerId = this.ownerDetailMemberInfo.ownerId
      this.$refs.deleteOwner.open(member)
    },
    _openEditOwnerModel(member) {
      member.ownerId = this.ownerDetailMemberInfo.ownerId
      this.$refs.editOwnerMember.open(member)
    },
    _viewOwnerFace(url) {
      this.$emit('viewImage', url)
    },
    handleCurrentChange(val) {
      this._loadOwnerDetailMemberData(val, this.pagination.pageSize)
    },
  },
  created() {
    this.$on('switch', (data) => {
      this.ownerDetailMemberInfo.ownerId = data.ownerId
      this._loadOwnerDetailMemberData(1, this.pagination.pageSize)
    })
  }
}
</script>

<style scoped>
.margin-top {
  margin-top: 20px;
}

.margin-top-lg {
  margin-top: 30px;
}

.padding-right-xs {
  padding-right: 5px;
}

.padding-left-xl {
  padding-left: 20px;
}

.border-radius {
  border-radius: 4px;
}

.text-right {
  text-align: right;
}
</style>