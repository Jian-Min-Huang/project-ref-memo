using System;
using System.IO;
using System.Text;
using System.Threading;
using SKCOMLib;

namespace CapitalGateway
{
    public class CapitalApi
    {
        private SKCenterLib m_pSKCenterLib;
        private SKQuoteLib m_pSKQuoteLib;
        private SKOrderLib m_pSKOrderLib;

        private StringBuilder sbData = new StringBuilder();
        
        public CapitalApi()
        {
            init();
            login("H123732745", "cDob092187l");
            connectQuotoServer();
            connectOrderServer("H123732745");
        }
        
        private void init()
        {
            m_pSKCenterLib = new SKCenterLib();
            m_pSKQuoteLib = new SKQuoteLib();
            m_pSKOrderLib = new SKOrderLib();
        }

        private void login(string userId, string password)
        {
            int m_nCode = m_pSKCenterLib.SKCenterLib_Login(userId, password);

            if (m_nCode == 0)
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "Login Success !");
            else
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "Login Fail, Code : " + m_nCode);
        }

        private void connectQuotoServer()
        {
            m_pSKQuoteLib.OnConnection += new _ISKQuoteLibEvents_OnConnectionEventHandler(m_SKQuoteLib_OnConnection);
            m_pSKQuoteLib.OnNotifyKLineData += new _ISKQuoteLibEvents_OnNotifyKLineDataEventHandler(m_SKQuoteLib_OnNotifyKLineData);
            m_pSKQuoteLib.SKQuoteLib_EnterMonitor();
        }

        private void connectOrderServer(string userId)
        {
            var m_nCode1 = m_pSKOrderLib.SKOrderLib_Initialize();
            var m_nCode2 = m_pSKOrderLib.ReadCertByID(userId);
            var m_nCode3 = m_pSKOrderLib.GetUserAccount();
            
            Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + m_nCode1 + " / " + m_nCode2 + " / " + m_nCode3);
        }

        private void m_SKQuoteLib_OnConnection(int nKind, int nCode)
        {
            if (nKind == 3001)
            {
                if (nCode == 0) Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "Yellow");
            }
            else if (nKind == 3002)
            {
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "Red");
            }
            else if (nKind == 3003)
            {
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "Green");
            }
            else if (nKind == 3021) //網路斷線
            {
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "DrakRed");
            }
        }

        private void m_SKQuoteLib_OnNotifyKLineData(string bstrStockNo, string bstrData)
        {
            var replacedComma = bstrData.Replace(", ", ",");
            var line = bstrStockNo + "," + replacedComma + "\n";
            sbData.Append(line);
            
            Console.Write("[" + Thread.CurrentThread.Name + "] " + line);
        }

        public void requestData(string name, short type)
        {
            // 0 = minute, 4 = day
            // 0 = old output type, 1 = new output type
            // 0 = all day, 1 = only AM
            m_pSKQuoteLib.SKQuoteLib_RequestKLineAM(name, type, 1, 1);
        }

        public void writeFile(string name, string type)
        {
            int count = 0, sizeO = 0, sizeN = 0;
            while (count < 3)
            {
                if (sizeO == 0) sizeO = sbData.ToString().Length;
                sizeN = sbData.ToString().Length;
                if (sizeO == sizeN) count++;
                
                Console.WriteLine("[" + Thread.CurrentThread.Name + "] " + "(" + sizeO + ", " + sizeN + ") found equals, count = " + count + " ... ");
                
                Thread.Sleep(3000);
            }
            
            File.WriteAllText("C:\\Users\\yFr\\Desktop\\" + name + type + ".csv", sbData.ToString());
            sbData.Clear();
        }
    }
}