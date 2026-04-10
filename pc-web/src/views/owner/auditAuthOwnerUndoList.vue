<template>
  <div class="audit-auth-owner-undo-container">
    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('auditAuthOwnerUndo.title') }}</span>
        <el-button style="float: right; padding: 3px 0" type="text" @click="goBack">
          {{ $t('auditAuthOwnerUndo.back') }}
        </el-button>
      </div>
      <el-row :gutter="20" v-if="auditAuthOwnerUndoInfo.room">
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.roomNumber') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.roomName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.roomArea') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.builtUpArea }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.roomType') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.roomSubTypeName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.roomStatus') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.stateName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.owner') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.ownerName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.ownerPhone') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.room.link }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.authPerson') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.appUserName }}</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="form-item">
            <label class="form-label">{{ $t('auditAuthOwnerUndo.ownerPhone') }}</label>
            <div class="form-content">{{ auditAuthOwnerUndoInfo.link }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('auditAuthOwnerUndo.housePersonnel') }}</span>
      </div>
      <el-table :data="auditAuthOwnerUndoInfo.owners" border style="width: 100%">
        <el-table-column prop="url" :label="$t('auditAuthOwnerUndo.ownerFace')" align="center">
          <template slot-scope="scope">
            <el-image
              style="width: 60px; height: 60px;"
              :src="scope.row.url || '/img/noPhoto.jpg'"
              :preview-src-list="[scope.row.url]"
              fit="cover"
              class="border-radius"
              @click="viewOwnerFace(scope.row.url)"
            ></el-image>
          </template>
        </el-table-column>
        <el-table-column prop="name" :label="$t('auditAuthOwnerUndo.name')" align="center"></el-table-column>
        <el-table-column prop="link" :label="$t('auditAuthOwnerUndo.contact')" align="center"></el-table-column>
        <el-table-column prop="address" :label="$t('auditAuthOwnerUndo.address')" align="center">
          <template slot-scope="scope">
            {{ scope.row.address || '-' }}
          </template>
        </el-table-column>
        <el-table-column prop="idCard" :label="$t('auditAuthOwnerUndo.idCard')" align="center">
          <template slot-scope="scope">
            {{ scope.row.idCard || '-' }}
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="box-card">
      <div slot="header" class="flex justify-between">
        <span>{{ $t('auditAuthOwnerUndo.audit') }}</span>
      </div>
      <el-form label-width="120px">
        <el-form-item :label="$t('auditAuthOwnerUndo.action')">
          <el-select v-model="auditAuthOwnerUndoInfo.audit.state" style="width: 100%">
            <el-option disabled :value="''" :label="$t('auditAuthOwnerUndo.selectAudit')"></el-option>
            <el-option value="1100" :label="$t('auditAuthOwnerUndo.agree')"></el-option>
            <el-option value="1200" :label="$t('auditAuthOwnerUndo.reject')"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item :label="$t('auditAuthOwnerUndo.auditRemark')">
          <el-input
            type="textarea"
            :placeholder="$t('auditAuthOwnerUndo.requiredRemark')"
            v-model="auditAuthOwnerUndoInfo.audit.remark"
            :rows="4"
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="auditSubmit">{{ $t('auditAuthOwnerUndo.submit') }}</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { getRoomInfo, getOwnerList, auditAuthOwner } from '@/api/owner/auditAuthOwnerUndoApi'

export default {
  name: 'AuditAuthOwnerUndoList',
  data() {
    return {
      auditAuthOwnerUndoInfo: {
        room: {},
        roomId: '',
        owners: [],
        appUserName: '',
        link: '',
        audit: {
          state: '',
          remark: '',
          appUserId: ''
        }
      }
    }
  },
  created() {
    this.initData()
  },
  methods: {
    initData() {
      this.auditAuthOwnerUndoInfo.audit.appUserId = this.$route.query.appUserId
      this.auditAuthOwnerUndoInfo.roomId = this.$route.query.roomId
      this.auditAuthOwnerUndoInfo.appUserName = this.$route.query.appUserName
      this.auditAuthOwnerUndoInfo.link = this.$route.query.link
      this.loadRoom()
      this.loadOwners()
    },
    async loadRoom() {
      try {
        const params = {
          page: 1,
          row: 1,
          roomId: this.auditAuthOwnerUndoInfo.roomId,
          communityId: getCommunityId()
        }
        const res = await getRoomInfo(params)
        this.auditAuthOwnerUndoInfo.room = res.rooms[0]
      } catch (error) {
        console.error('Failed to load room info:', error)
      }
    },
    async loadOwners() {
      try {
        const params = {
          page: 1,
          row: 100,
          roomId: this.auditAuthOwnerUndoInfo.roomId,
          communityId: getCommunityId()
        }
        const res = await getOwnerList(params)
        this.auditAuthOwnerUndoInfo.owners = res.data
      } catch (error) {
        console.error('Failed to load owners:', error)
      }
    },
    viewOwnerFace(url) {
      if (url) {
        // 预览图片逻辑
      }
    },
    async auditSubmit() {
      try {
        const auditInfo = {
          ...this.auditAuthOwnerUndoInfo.audit,
          communityId: getCommunityId()
        }
        const res = await auditAuthOwner(auditInfo)
        if (res.code !== 0) {
          this.$message.error(res.msg)
          return
        }
        this.$message.success(this.$t('common.operationSuccess'))
        this.$router.go(-1)
      } catch (error) {
        this.$message.error(this.$t('auditAuthOwnerUndo.submitFailed') + error)
      }
    },
    goBack() {
      this.$router.go(-1)
    }
  }
}
</script>

<style lang="scss" scoped>
.audit-auth-owner-undo-container {
  padding: 20px;

  .box-card {
    margin-bottom: 20px;
  }

  .form-item {
    margin-bottom: 18px;

    .form-label {
      color: #606266;
      margin-right: 10px;
    }

    .form-content {
      display: inline-block;
    }
  }

  .border-radius {
    border-radius: 4px;
  }
}
</style>