package com.uflycn.uoperation.ui.splash.model;

import android.content.Context;


import com.uflycn.uoperation.bean.RegistrationInfo;
import com.uflycn.uoperation.constant.AppConstant;
import com.uflycn.uoperation.util.AESUtils;
import com.uflycn.uoperation.util.IOUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by Ryan on 2016/7/13.
 */
public class SplashModelImpl implements SplashModel {

    private Context mContext;

    public SplashModelImpl(Context context) {
        mContext = context;
    }

    @Override
    public boolean isFileExist() {
        String path = IOUtils.getRootStoragePath(mContext);
        String wholePath = path+ AppConstant.REGISTER_KEY_PATH+ AppConstant.REGISTER_KEY_NAME;
        File file = new File(wholePath);
        return file.exists();
    }

    @Override
    public String getRegisterFileCode() {
        if (!isFileExist()) {
            return "";
        }

        String path = IOUtils.getRootStoragePath(mContext);
        String wholePath = path + AppConstant.REGISTER_KEY_PATH + AppConstant.REGISTER_KEY_NAME;
        File file = new File(wholePath);
        FileReader fr = null;
        BufferedReader br = null;
        StringBuilder sb = null;
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            return AESUtils.decrypt(sb.toString(), AppConstant.REGISTER_SEED);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fr != null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return "";

    }

    @Override
    public void saveRegisterFile(String message) {
        if (isFileExist()) {
            String path = IOUtils.getRootStoragePath(mContext);
            String wholePath = path+ AppConstant.REGISTER_KEY_PATH+ AppConstant.REGISTER_KEY_NAME;
            File file = new File(wholePath);
            FileWriter fw = null;
            try {
                fw = new FileWriter(file, false);
                fw.write(message);
                fw.close();
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }else {
            String basePath = IOUtils.getRootStoragePath(mContext);
            String registerPath = basePath + AppConstant.REGISTER_KEY_PATH;
            String wholePath = registerPath+ AppConstant.REGISTER_KEY_NAME;
            File file = new File(registerPath);
            if (!file.exists())  {
                file.mkdirs();
            }

            System.out.println(wholePath);
            FileWriter fw = null;
            File keyFile = new File(wholePath);
            try {
                if (!keyFile.exists()) {
                    keyFile.createNewFile();
                }
                fw = new FileWriter(keyFile, false);
                fw.write(message);
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (fw != null) {
                    try {
                        fw.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void saveRegInfoFile(RegistrationInfo info) {
        String path = IOUtils.getRootStoragePath(mContext);
        String wholePath = path + AppConstant.REGISTER_KEY_PATH;
        File file = new File(wholePath + AppConstant.REGISTER_INFO_NAME);
        File p = new File(wholePath);
        if (!p.exists()) {
            p.mkdirs();
        }

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        FileWriter fw = null;
        try {
            fw = new FileWriter(file, false);
            fw.write(AESUtils.encrypt(info.getName(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getCompany(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getDeviceNo(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getProductName(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getApplicant(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getTel(),AppConstant.REGISTER_SEED));
            fw.write(AESUtils.encrypt(info.getEmail(),AppConstant.REGISTER_SEED));
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fw != null) {
                try {
                    fw.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public RegistrationInfo getRegInfo() {
        String path = IOUtils.getRootStoragePath(mContext);
        String wholePath = path + AppConstant.REGISTER_KEY_PATH;
        File file = new File(wholePath + AppConstant.REGISTER_INFO_NAME);
        if (!file.exists()) {
            return null;
        }

        FileReader fr = null;
        BufferedReader br = null;
        RegistrationInfo registrationInfo = new RegistrationInfo();
        try {
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String line = null;
            int index = 0;
            while ((line = br.readLine()) != null) {
                if (index == 0) {
                    registrationInfo.setName(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 1) {
                    registrationInfo.setCompany(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }else if (index == 2) {
                    registrationInfo.setDeviceNo(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }else if (index == 3) {
                    registrationInfo.setProductName(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }else if (index == 4) {
                    registrationInfo.setApplicant(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }else if (index == 5) {
                    registrationInfo.setTel(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                } else if (index == 6) {
                    registrationInfo.setEmail(AESUtils.decrypt(line, AppConstant.REGISTER_SEED));
                }
                index ++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (br != null){
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fr != null){
                try {
                    fr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }

        return registrationInfo;
    }

    @Override
    public String createCode(String imei) {
        try {
            return AESUtils.encrypt(AppConstant.REGISTER_SEED,imei);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public void deleteRegisterFile() {
        String path = IOUtils.getRootStoragePath(mContext);
        String wholePath = path + AppConstant.REGISTER_KEY_PATH + AppConstant.REGISTER_KEY_NAME;
        File file = new File(wholePath);
        if (file.exists()) {
            file.delete();
        }
    }
}
