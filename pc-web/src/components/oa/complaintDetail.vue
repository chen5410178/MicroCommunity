<template>
  <el-dialog :title="`${form.typeName} ${$t('complaint.detail.title')}`" :visible.sync="visible" width="70%"
    top="5vh">
    <el-form label-width="120px">
      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.room')">
            <el-input v-model="form.roomName" readonly />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.id')">
            <el-input v-model="form.complaintId" readonly />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.type')">
            <el-input v-model="form.typeName" readonly />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.status')">
            <el-input v-model="form.stateName" readonly />
          </el-form-item>
        </el-col>
      </el-row>

      <el-row :gutter="20">
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.contact')">
            <el-input v-model="form.complaintName" readonly />
          </el-form-item>
        </el-col>
        <el-col :span="12">
          <el-form-item :label="$t('complaint.detail.phone')">
            <el-input v-model="form.tel" readonly />
          </el-form-item>
        </el-col>
      </el-row>

      <template v-if="form.showCurrentUser">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item :label="$t('complaint.detail.handler')">
              <el-input v-model="form.currentUserName" readonly />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item :label="$t('complaint.detail.handlerPhone')">
              <el-input v-model="form.currentUserTel" readonly />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item :label="$t('complaint.detail.handlerId')">
          <el-input v-model="form.currentUserId" readonly />
        </el-form-item>
      </template>

      <el-form-item :label="`${$t('complaint.detail.content')}`">
        <el-input type="textarea" :rows="4" v-model="form.context" readonly />
      </el-form-item>

      <el-form-item :label="$t('complaint.detail.photos')" v-if="form.photosShow.length > 0">
        <div style="display:flex;flex-wrap:wrap;gap:10px">
          <el-image v-for="(photo, index) in form.photosShow" :key="index" style="width:100px;height:100px"
            :src="photo.url" :preview-src-list="form.photosShow.map(p => p.url)" fit="cover" />
        </div>
      </el-form-item>
    </el-form>

    <el-table :data="form.comments" border style="width:100%;margin-top:20px">
      <el-table-column prop="index" :label="$t('complaint.detail.serial')" width="80" align="center">
        <template slot-scope="scope">
          {{ scope.$index + 1 }}
        </template>
      </el-table-column>
      <el-table-column prop="auditName" :label="$t('complaint.detail.handler')" align="center" />
      <el-table-column prop="stateName" :label="$t('complaint.detail.status')" align="center" />
      <el-table-column prop="auditTime" :label="$t('complaint.detail.handleTime')" align="center" />
      <el-table-column prop="duration" :label="$t('complaint.detail.duration')" align="center" />
      <el-table-column prop="message" :label="$t('complaint.detail.comment')" align="center" />
    </el-table>
  </el-dialog>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { listWorkflowAuditInfo } from '@/api/oa/complaintApi'

export default {
  name: 'ComplaintDetail',
  data() {
    return {
      visible: false,
      form: {
        complaintId: '',
        typeCd: '',
        complaintName: '',
        tel: '',
        context: '',
        typeName: '',
        stateName: '',
        roomName: '',
        currentUserName: '',
        currentUserTel: '',
        currentUserId: '',
        showCurrentUser: true,
        photos: [],
        photosShow: [],
        comments: []
      }
    }
  },
  methods: {
    open(row) {
      this.visible = true
      console.log(row)
      this.form = {
        ...row,
        photosShow: row.photos.map(photo => ({
          url: `/callComponent/download/getFile/file?fileId=${photo.url}&communityId=-1&time=${new Date()}`
        })),
        showCurrentUser: row['currentUserName'],
        currentUserName: row.currentUserName || this.$t('common.none'),
        currentUserTel: row.currentUserTel || this.$t('common.none'),
        currentUserId: row.currentUserId || this.$t('common.none')
      }
      this.loadComments()
    },
    async loadComments() {
      try {
        const params = {
          communityId: getCommunityId(),
          businessKey: this.form.complaintId
        }
        const { data } = await listWorkflowAuditInfo(params)
        this.form.comments = data
      } catch (error) {
        console.error('获取处理记录失败:', error)
      }
    }
  }
}
</script>