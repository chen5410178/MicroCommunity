<template>
  <div class="admin-owner-container">
    <el-row :gutter="20">
      <el-col :span="3">
        <select-admin-community ref="selectCommunity" @changeCommunity="handleCommunityChange"></select-admin-community>
      </el-col>
      <el-col :span="21">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminOwner.searchTitle') }}</span>
          </div>
          <el-form :inline="true" :model="searchForm" class="search-form ">
            <el-form-item >
              <el-select v-model="searchForm.personRole" :placeholder="$t('adminOwner.selectPersonRole')">
                <el-option :label="$t('adminOwner.all')" value=""></el-option>
                <el-option v-for="item in personRoles" :key="item.statusCd" :label="item.name"
                  :value="item.statusCd"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item >
              <el-select v-model="searchForm.personType" :placeholder="$t('adminOwner.selectPersonType')">
                <el-option :label="$t('adminOwner.all')" value=""></el-option>
                <el-option :label="$t('adminOwner.personal')" value="P"></el-option>
                <el-option :label="$t('adminOwner.company')" value="C"></el-option>
              </el-select>
            </el-form-item>

            <el-form-item >
              <el-input v-model="searchForm.name" :placeholder="$t('adminOwner.inputCustomerName')"></el-input>
            </el-form-item>

            <el-form-item >
              <el-input v-model="searchForm.roomName" :placeholder="$t('adminOwner.inputRoomNumber')"></el-input>
            </el-form-item>

            <el-form-item >
              <el-input v-model="searchForm.link" :placeholder="$t('adminOwner.inputContact')" type="number"></el-input>
            </el-form-item>

           

            <el-form-item>
              <el-button type="primary" @click="queryOwner">{{ $t('adminOwner.search') }}</el-button>
              <el-button @click="resetOwner">{{ $t('adminOwner.reset') }}</el-button>
            </el-form-item>
            <el-form-item >
              <el-input v-model="searchForm.idCard" :placeholder="$t('adminOwner.inputIdCard')"></el-input>
            </el-form-item>
          </el-form>
        </el-card>

        <el-card class="box-card" style="margin-top: 20px;">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('adminOwner.ownerInfo') }}</span>
          </div>
          <el-table :data="ownerList" border style="width: 100%">
            <el-table-column prop="faceUrl" :label="$t('adminOwner.face')" width="100" align="center">
              <template slot-scope="scope">
                <el-image style="width: 60px; height: 60px;" :src="scope.row.faceUrl || '/img/noPhoto.jpg'"
                  :preview-src-list="[scope.row.faceUrl || '/img/noPhoto.jpg']" fit="cover" class="border-radius">
                </el-image>
              </template>
            </el-table-column>

            <el-table-column prop="communityName" :label="$t('adminOwner.communityName')"
              align="center"></el-table-column>
            <el-table-column prop="name" :label="$t('adminOwner.customerName')" align="center">
              <template slot-scope="scope">
                <div>{{ scope.row.name }}</div>
                <div v-if="scope.row.personType === 'C'">{{ scope.row.concactPerson }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="personTypeName" :label="$t('adminOwner.personType')" align="center"></el-table-column>
            <el-table-column prop="personRoleName" :label="$t('adminOwner.personRole')" align="center"></el-table-column>
            <el-table-column prop="sex" :label="$t('adminOwner.gender')" align="center">
              <template slot-scope="scope">
                {{ scope.row.sex === 0 ? $t('adminOwner.male') : $t('adminOwner.female') }}
              </template>
            </el-table-column>
            <el-table-column prop="idCard" :label="$t('adminOwner.idCard')" align="center">
              <template slot-scope="scope">
                {{ scope.row.idCard || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="link" :label="$t('adminOwner.contactPhone')" align="center"></el-table-column>
            <el-table-column prop="concactLink" :label="$t('adminOwner.backupPhone')" align="center">
              <template slot-scope="scope">
                {{ scope.row.concactLink || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="address" :label="$t('adminOwner.address')" align="center">
              <template slot-scope="scope">
                {{ scope.row.address || '-' }}
              </template>
            </el-table-column>
            <el-table-column prop="roomCount" :label="$t('adminOwner.roomCount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.roomCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="memberCount" :label="$t('adminOwner.memberCount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.memberCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="carCount" :label="$t('adminOwner.carCount')" align="center">
              <template slot-scope="scope">
                {{ scope.row.carCount || 0 }}
              </template>
            </el-table-column>
            <el-table-column prop="oweFee" :label="$t('adminOwner.oweFee')" align="center">
              <template slot-scope="scope">
                {{ scope.row.oweFee || '0.00' }}
              </template>
            </el-table-column>
            <el-table-column :label="$t('adminOwner.operation')" align="center" width="120">
              <template slot-scope="scope">
                <el-button type="text" size="small" @click="toOwnerDetail(scope.row)">
                  {{ $t('adminOwner.detail') }}
                </el-button>
              </template>
            </el-table-column>
          </el-table>

          <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
            :current-page="pagination.currentPage" :page-sizes="[10, 20, 30, 50]" :page-size="pagination.pageSize"
            layout="total, sizes, prev, pager, next, jumper" :total="pagination.total" style="margin-top: 20px;">
          </el-pagination>
        </el-card>
      </el-col>
    </el-row>

    <view-image ref="viewImage"></view-image>
  </div>
</template>

<script>
import { queryAdminOwners } from '@/api/owner/adminOwnerApi'
import SelectAdminCommunity from '@/components/community/selectAdminCommunity'
import ViewImage from '@/components/owner/viewImage'
import { getDict } from '@/api/community/communityApi'

export default {
  name: 'AdminOwnerList',
  components: {
    SelectAdminCommunity,
    ViewImage
  },
  data() {
    return {
      searchForm: {
        personRole: '',
        personType: '',
        name: '',
        roomName: '',
        link: '',
        idCard: '',
        communityId: ''
      },
      ownerList: [],
      personRoles: [],
      pagination: {
        currentPage: 1,
        pageSize: 10,
        total: 0
      }
    }
  },
  created() {
    this.loadPersonRoles()
    this.queryOwner()
  },
  methods: {
    async loadPersonRoles() {
      const data = await getDict('building_owner', 'person_role')
      this.personRoles = [...data]

    },
    queryOwner() {
      const params = {
        ...this.searchForm,
        page: this.pagination.currentPage,
        row: this.pagination.pageSize
      }

      queryAdminOwners(params).then(res => {
        this.ownerList = res.data
        this.pagination.total = res.total
      }).catch(error => {
        console.log(error)
        this.$message.error(this.$t('adminOwner.fetchError'))
      })
    },
    resetOwner() {
      this.searchForm = {
        personRole: '',
        personType: '',
        name: '',
        roomName: '',
        link: '',
        idCard: '',
        communityId: this.searchForm.communityId
      }
      this.pagination.currentPage = 1
      this.queryOwner()
    },
    handleCommunityChange(community) {
      this.searchForm.communityId = community.communityId
      this.queryOwner()
    },
    handleSizeChange(val) {
      this.pagination.pageSize = val
      this.queryOwner()
    },
    handleCurrentChange(val) {
      this.pagination.currentPage = val
      this.queryOwner()
    },
    toOwnerDetail(owner) {
      this.$router.push(`/views/aCommunity/adminOwnerDetail?ownerId=${owner.ownerId}`)
    },
    viewOwnerFace(url) {
      this.$refs.viewImage.open(url)
    }
  }
}
</script>

<style lang="scss" scoped>
.admin-owner-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .search-form {
    text-align: left;
    .el-form-item {
      margin-bottom: 10px;
    }
  }

  .border-radius {
    border-radius: 4px;
  }
}
</style>