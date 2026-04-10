<template>
  <div class="margin-top">
    <el-row class="margin-top-lg">
      <el-col :span="4" class="padding-right-xs padding-left-xl">
        <el-input v-model="aRoomDetailMemberInfo.name" type="text"
          :placeholder="$t('aRoomDetailMember.placeholderName')"></el-input>
      </el-col>
      <el-col :span="4" class="padding-right-xs padding-right-xl">
        <el-button type="primary" size="small" style="margin-left:10px" @click="_qureyARoomDetailMember()">
          <i class="el-icon-search"></i>{{ $t('common.search') }}
        </el-button>
      </el-col>
      <el-col :span="16" class="text-right"></el-col>
    </el-row>
    <div class="margin-top">
      <el-table :data="aRoomDetailMemberInfo.members" style="width: 100%">
        <el-table-column prop="faceUrl" :label="$t('aRoomDetailMember.face')" align="center">
          <template slot-scope="scope">
            <img style="width: 60px; height: 60px;" class="border-radius" v-if="scope.row.faceUrl"
              :src="scope.row.faceUrl" @click="_viewOwnerFace(scope.row.faceUrl)" />
            <img style="width: 60px; height: 60px;" class="border-radius" v-else src="/img/noPhoto.jpg" />
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('aRoomDetailMember.name')" align="center"></el-table-column>
        <el-table-column prop="sex" :label="$t('aRoomDetailMember.sex')" align="center">
          <template slot-scope="scope">{{ scope.row.sex == 0 ? $t('common.male') : $t('common.female') }}</template>
        </el-table-column>
        <el-table-column prop="personRoleName" :label="$t('aRoomDetailMember.personRole')"
          align="center"></el-table-column>
        <el-table-column prop="idCard" :label="$t('aRoomDetailMember.idCard')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('aRoomDetailMember.contact')" align="center"></el-table-column>
        <el-table-column prop="address" :label="$t('aRoomDetailMember.address')" align="center"></el-table-column>
      </el-table>
      <el-row>
        <el-col :span="16" :offset="8">
          <el-pagination @current-change="handleCurrentChange" :current-page="currentPage" :page-size="pageSize"
            layout="total, prev, pager, next" :total="total">
          </el-pagination>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { queryAdminOwnerMembers } from '@/api/aCommunity/aRoomDetailMemberApi'

export default {
  name: 'ARoomDetailMember',
  data() {
    return {
      aRoomDetailMemberInfo: {
        members: [],
        ownerId: '',
        name: '',
      },
      currentPage: 1,
      pageSize: 10,
      total: 0
    }
  },
  created() {
  },
  methods: {
    open(data) {
      this.aRoomDetailMemberInfo.ownerId = data.ownerId
      this._loadARoomDetailMemberData(this.currentPage, this.pageSize)
    },
    handleSwitch(data) {
      this.aRoomDetailMemberInfo.ownerId = data.ownerId
      this._loadARoomDetailMemberData(this.currentPage, this.pageSize)
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this._loadARoomDetailMemberData(val, this.pageSize)
    },
    async _loadARoomDetailMemberData(page, row) {
      if (!this.aRoomDetailMemberInfo.ownerId) return

      try {
        const res = await queryAdminOwnerMembers({
          ownerId: this.aRoomDetailMemberInfo.ownerId,
          name: this.aRoomDetailMemberInfo.name,
          page,
          row
        })
        this.aRoomDetailMemberInfo.members = res.data
        this.total = res.total
      } catch (error) {
        console.error('请求失败:', error)
      }
    },
    _qureyARoomDetailMember() {
      this.currentPage = 1
      this._loadARoomDetailMemberData(1, this.pageSize)
    },
    _viewOwnerFace(url) {
      this.$emit('viewImage', url)
    }
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
  padding-right: 10px;
}

.padding-left-xl {
  padding-left: 20px;
}

.border-radius {
  border-radius: 4px;
}
</style>