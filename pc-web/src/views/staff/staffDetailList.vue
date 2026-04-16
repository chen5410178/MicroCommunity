<template>
  <div class="staff-detail-container">
    <el-row :gutter="20">
      <el-col :span="18">
        <el-card class="box-card">
          <div slot="header" class="clearfix flex justify-between">
            <span>{{ $t('staffDetailInfo.title') }}</span>
            <el-button style="padding: 3px 0" type="text" @click="goBack">
              {{ $t('common.back') }}
            </el-button>
          </div>
          <div class="staff-info">
            <el-row :gutter="20">
              <el-col :span="4">
                <el-image style="width: 120px; height: 140px; border-radius: 4px;" :src="staffDetailInfo.photoUrl"
                  fit="cover" @error="errorLoadImg">
                </el-image>
              </el-col>
              <el-col :span="20">
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="info-item">
                      <label>{{ $t('staffDetailInfo.staffId') }}</label>
                      <div>{{ staffDetailInfo.staffId }}</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <label>{{ $t('staffDetailInfo.userName') }}</label>
                      <div>{{ staffDetailInfo.userName }}</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item ">
                      <label>{{ $t('staffDetailInfo.email') }}</label>
                      <div>{{ staffDetailInfo.email }}</div>
                    </div>
                  </el-col>
                </el-row>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="info-item">
                      <label>{{ $t('staffDetailInfo.tel') }}</label>
                      <div>{{ staffDetailInfo.tel }}</div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <label>{{ $t('staffDetailInfo.sex') }}</label>
                      <div>{{ staffDetailInfo.sex == '0' ? $t('staffDetailInfo.male') : $t('staffDetailInfo.female') }}
                      </div>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <label>{{ $t('staffDetailInfo.address') }}</label>
                      <div>{{ staffDetailInfo.address }}</div>
                    </div>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          </div>
        </el-card>

        <el-card class="box-card margin-top text-left">
          <div slot="header" class="clearfix ">
            <span>{{ $t('staffDetailInfo.relatedOrg') }}</span>
          </div>
          <div v-for="(item, index) in staffDetailInfo.orgs" :key="index">
            <div class="org-item">{{ $t('staffDetailInfo.org') }}: {{ item.orgName }}</div>
          </div>
        </el-card>

        <el-card class="box-card margin-top text-left">
          <div slot="header" class="clearfix">
            <span>{{ $t('staffDetailInfo.relatedRoleCommunity') }}</span>
          </div>
          <div v-for="(item, index) in staffDetailInfo.roles" :key="index">
            <div class="flex justify-start">
             <div> {{ item.roleName }} </div> <div v-if="item.roleCommunityDtoList.length > 0">(
              <span v-for="(item1, index1) in item.roleCommunityDtoList" :key="index1">
                {{ item1.communityName }}
                {{ index1 === item.roleCommunityDtoList.length - 1 ? " " : " 、 " }}
              </span> )
              </div>
            </div>
          </div>
        </el-card>

        <!-- <staff-map-pos :staff-id="staffDetailInfo.staffId" class="margin-top"></staff-map-pos> -->
      </el-col>

      <el-col :span="6">
        <el-card class="box-card text-left">
          <div slot="header" class="clearfix">
            <span>{{ $t('staffDetailInfo.staffPrivilege') }}</span>
          </div>
          <el-tree :data="privilegeTreeData" node-key="id" default-expand-all :props="defaultProps">
          </el-tree>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getStaffDetail, getStaffOrgs, getStaffRoles, getStaffPrivileges } from '@/api/staff/staffDetailApi'

export default {
  name: 'StaffDetailList',
  components: {
  },
  data() {
    return {
      staffDetailInfo: {
        staffId: '',
        userName: '',
        email: '',
        tel: '',
        sex: '',
        address: '',
        photo: '',
        orgs: [],
        roles: []
      },
      privilegeTreeData: [],
      defaultProps: {
        children: 'children',
        label: 'text'
      }
    }
  },
  created() {
    this.staffDetailInfo.staffId = this.$route.query.staffId
    this.loadStaffDetail()
    this.loadStaffOrgs()
    this.loadStaffRoles()
    this.loadStaffPrivileges()
  },
  methods: {
    goBack() {
      this.$router.go(-1)
    },
    errorLoadImg() {
      this.staffDetailInfo.photoUrl = "/img/noPhoto.jpg"
    },
    async loadStaffDetail() {
      try {
        const params = {
          page: 1,
          row: 1,
          staffId: this.staffDetailInfo.staffId
        }
        const res = await getStaffDetail(params)
        if ( res.staffs.length > 0) {
          Object.assign(this.staffDetailInfo, res.staffs[0])
          this.staffDetailInfo.photoUrl = res.staffs[0].photoUrl
        }
      } catch (error) {
        this.$message.error(this.$t('staffDetailInfo.fetchError'))
      }
    },
    async loadStaffOrgs() {
      try {
        const params = {
          page: 1,
          row: 1,
          staffId: this.staffDetailInfo.staffId
        }
        const res = await getStaffOrgs(params)
        if (res.code === 0) {
          this.staffDetailInfo.orgs = res.data
        }
      } catch (error) {
        this.$message.error(this.$t('staffDetailInfo.fetchOrgError'))
      }
    },
    async loadStaffRoles() {
      try {
        const params = {
          page: 1,
          row: 9999,
          staffId: this.staffDetailInfo.staffId
        }
        const res = await getStaffRoles(params)
        if (res.code == 0) {
          this.staffDetailInfo.roles = res.data
        }
      } catch (error) {
        this.$message.error(this.$t('staffDetailInfo.fetchRoleError'))
      }
    },
    async loadStaffPrivileges() {
      try {
        const params = {
          staffId: this.staffDetailInfo.staffId
        }
        const res = await getStaffPrivileges(params)
        if (res.datas && res.datas.length > 0) {
          this.privilegeTreeData = this.buildTreeData(res.datas)
        }
      } catch (error) {
        this.$message.error(this.$t('staffDetailInfo.fetchPrivilegeError'))
      }
    },
    buildTreeData(privileges) {
      const groupMap = {}

      privileges.forEach(item => {
        if (!groupMap[item.gId]) {
          groupMap[item.gId] = {
            id: `g_${item.gId}`,
            gId: item.gId,
            text: item.gName,
            children: []
          }
        }

        const group = groupMap[item.gId]
        const menuExists = group.children.some(menu => menu.mId === item.mId)

        if (!menuExists) {
          group.children.push({
            id: `m_${item.mId}`,
            mId: item.mId,
            text: item.mName,
            children: []
          })
        }

        const menu = group.children.find(menu => menu.mId === item.mId)
        menu.children.push({
          id: `p_${item.pId}`,
          pId: item.pId,
          text: item.pName
        })
      })

      return Object.values(groupMap)
    }
  }
}
</script>

<style lang="scss" scoped>
.staff-detail-container {
  padding: 20px;

  .margin-top {
    margin-top: 20px;
  }

  .info-item {
    margin-bottom: 15px;


    label {
      display: block;
      color: #909399;
      margin-bottom: 5px;
    }
  }

  .org-item {
    padding: 5px 0;
  }
}
</style>