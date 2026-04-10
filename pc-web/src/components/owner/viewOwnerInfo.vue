<template>
  <el-card class="box-card">
    <div slot="header" class="flex justify-between">
      <span>{{ $t('viewOwnerInfo.title') }}</span>
      <div class="card-header-buttons">
        <el-button v-if="showCallBackButton" type="primary" size="small" @click="handleCallBack">
          <i class="el-icon-close"></i>
          {{ $t('common.back') }}
        </el-button>
        <el-button v-if="viewOwnerFlag !== 'Owner'" type="primary" size="small" @click="openChooseOwner">
          {{ $t('viewOwnerInfo.chooseOwner') }}
        </el-button>
      </div>
    </div>

    <div class="content-wrapper">
      <el-row :gutter="20">
        <el-col :span="3">
          <el-image style="width: 120px; height: 140px" :src="ownerPhoto" fit="cover" @error="errorLoadImg"></el-image>
        </el-col>

        <el-col :span="21">
          <el-row :gutter="20" class="text-left">
            <el-col :span="8" >
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.ownerId') }}：</label>
                <span>{{ ownerId }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.name') }}：</label>
                <span>{{ name }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.sex') }}：</label>
                <span>{{ sex | formatSex }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="text-left">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.age') }}：</label>
                <span>{{ age }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.idCard') }}：</label>
                <span>{{ idCard }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.link') }}：</label>
                <span>{{ link }}</span>
              </div>
            </el-col>
          </el-row>

          <el-row :gutter="20" class="text-left">
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.userName') }}：</label>
                <span>{{ userName }}</span>
              </div>
            </el-col>
            <el-col :span="8">
              <div class="info-item">
                <label>{{ $t('viewOwnerInfo.remark') }}：</label>
                <span>{{ remark }}</span>
              </div>
            </el-col>
          </el-row>

          <template v-for="(item, index) in attrs">
            <el-row :gutter="20" :key="index" class="text-left" v-if="index % 3 === 0">
              <el-col :span="8">
                <div class="info-item">
                  <label>{{ item.specName }}：</label>
                  <span>{{ item.valueName }}</span>
                </div>
              </el-col>
              <el-col :span="8" v-if="index < attrs.length - 1">
                <div class="info-item">
                  <label>{{ attrs[index + 1].specName }}：</label>
                  <span>{{ attrs[index + 1].valueName }}</span>
                </div>
              </el-col>
              <el-col :span="8" v-if="index < attrs.length - 2">
                <div class="info-item">
                  <label>{{ attrs[index + 2].specName }}：</label>
                  <span>{{ attrs[index + 2].valueName }}</span>
                </div>
              </el-col>
            </el-row>
          </template>
        </el-col>
      </el-row>
    </div>

    <search-owner ref="searchOwner" @chooseOwner="handleChooseOwner" @loadData="handleLoadData" />
  </el-card>
</template>

<script>
import { getCommunityId } from '@/api/community/communityApi'
import { queryOwners } from '@/api/owner/deleteOwnerRoomApi'
import SearchOwner from './SearchOwner'

export default {
  name: 'ViewOwnerInfo',
  components: {
    SearchOwner
  },
  props: {
    showCallBackButton: {
      type: Boolean,
      default: false
    }
  },
  filters: {
    formatSex(value) {
      return value === '0' ? '男' : value === '1' ? '女' : ''
    }
  },
  data() {
    return {
      viewOwnerFlag: '',
      ownerId: '',
      name: '',
      age: '',
      sex: '',
      userName: '',
      remark: '',
      idCard: '',
      link: '',
      ownerPhoto: '/img/noPhoto.jpg',
      attrs: [],
      communityId: ''
    }
  },
  created() {
    this.communityId = getCommunityId()
    this.loadOwnerInfo()
  },
  methods: {
    async loadOwnerInfo() {
      const ownerId = this.$route.query.ownerId
      if (!ownerId) return

      this.viewOwnerFlag = 'Owner'

      try {
        const params = {
          ownerId,
          page: 1,
          row: 1,
          communityId: this.communityId,
          ownerTypeCd: '1001'
        }

        const res = await queryOwners(params)
          const owner = res.data[0]
          Object.assign(this, {
            ownerId: owner.memberId,
            name: owner.name,
            age: owner.age,
            sex: owner.sex,
            userName: owner.userName,
            remark: owner.remark,
            idCard: owner.idCard,
            link: owner.link,
            attrs: owner.ownerAttrDtos || []
          })

          if (owner.url) {
            this.ownerPhoto = owner.url
          }
        
      } catch (error) {
        console.error('Failed to load owner info:', error)
        this.$message.error(this.$t('viewOwnerInfo.loadError'))
      }
    },

    handleCallBack() {
      this.$emit('callBackListOwner', this.ownerId)
    },

    errorLoadImg() {
      this.ownerPhoto = '/img/noPhoto.jpg'
    },

    openChooseOwner() {
      this.$refs.searchOwner.open()
    },

    handleChooseOwner(owner) {
      Object.assign(this, {
        ownerId: owner.memberId,
        name: owner.name,
        age: owner.age,
        sex: owner.sex,
        userName: owner.userName,
        remark: owner.remark,
        idCard: owner.idCard,
        link: owner.link,
        ownerPhoto: owner.url || '/img/noPhoto.jpg',
        attrs: owner.ownerAttrDtos || []
      })
    },

    handleLoadData({ ownerId }) {
      this.ownerId = ownerId
      this.loadOwnerInfo()
    }
  }
}
</script>

<style scoped>
.box-card {
  margin-bottom: 20px;
}

.card-header-buttons {
  float: right;
}

.content-wrapper {
  padding: 20px;
}

.info-item {
  margin-bottom: 15px;
  line-height: 1.5;
  color: #606266;
}

.info-item label {
  margin-right: 5px;
}
</style>